package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.FileStorageService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bll.WithdrawalService;
import fr.eni.tp.auctionapp.bo.*;
import fr.eni.tp.auctionapp.dal.ItemDao;
import fr.eni.tp.auctionapp.dal.WithdrawalDao;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;
    private final WithdrawalDao withdrawalDao;
    private final FileStorageService fileStorageService;

    public ItemServiceImpl(ItemDao itemDao, WithdrawalDao withdrawalDao, FileStorageService fileStorageService) {
        this.itemDao = itemDao;
        this.withdrawalDao = withdrawalDao;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
    public void createItem(Item item) {
        BusinessException businessException = new BusinessException();

        if (item == null) {
            businessException.addKey("Item cannot be null");
        }

        if (item != null && item.getSeller() == null) {
            businessException.addKey("Seller cannot be null");
        }

        if (Objects.requireNonNull(item).getWithdrawal() == null) {
            Withdrawal defaultWithdrawal = new Withdrawal();
            User seller = item.getSeller();

            defaultWithdrawal.setStreet(seller.getStreet());
            defaultWithdrawal.setZipCode(seller.getZipCode());
            defaultWithdrawal.setCity(seller.getCity());

            defaultWithdrawal.setItem(item);
            item.setWithdrawal(defaultWithdrawal);
        }

        try {
            if(item.getImageFile() != null) {
                String imageFileName = fileStorageService.store(item.getImageFile());
                item.setImageUrl(imageFileName);
            }

            item.setSellingPrice(item.getStartingPrice());
            itemDao.insert(item);
            Objects.requireNonNull(item).getWithdrawal().setItem(item);
            Objects.requireNonNull(item).getWithdrawal().setItemId(item.getItemId());
            withdrawalDao.insert(item.getWithdrawal());
        } catch (BusinessException dalBusinessException) {
            throw dalBusinessException;
        }
    }

    @Override
    public Optional<Item> findItemById(int id) {
        Optional<Item> item = itemDao.findById(id);
        item.ifPresent(it -> it.setSaleStatus(calculateItemStatus(it)));
        return item;
    }

    @Override
    public void updateItem(Item item) {
            itemDao.update(item);
    }

    @Override
    public void handleItemUpdate(Item item, Authentication authentication, int itemId, int sellerId) throws BusinessException {
        BusinessException bException = new BusinessException();

        try {
            item.setItemId(itemId);
            User seller = new User();
            seller.setUserId(sellerId);
            item.setSeller(seller);

            isAuctionstarted(item);
            hasAuctionEnded(item);
            isUserAuthorized(item, authentication);

            updateItem(item);
            item.getWithdrawal().setItemId(itemId);
            withdrawalService.updateWithdrawal(item.getWithdrawal());

        } catch (BusinessException businessException) {
            throw businessException;
        } catch (Exception e) {
            bException.addKey("An unexpected error occurred during auction creation: " + e.getMessage());
            throw bException;
        }
    }

    private void isAuctionstarted(Item item) throws BusinessException {
        BusinessException businessException = new BusinessException();

        if (item.getAuctionStartingDate().isBefore(LocalDateTime.now())) {
            businessException.addKey("You can't perform this action as the auction has already started");
            throw businessException;
        }
    }

    private void hasAuctionEnded(Item item) throws BusinessException {
        BusinessException businessException = new BusinessException();

        if (item.getAuctionEndingDate().isBefore(LocalDateTime.now())) {
            businessException.addKey("You can't perform this action as the auction has already ended");
            throw businessException;
        }
    }

    private void isUserAuthorized(Item item, Authentication authentication) throws BusinessException {
        BusinessException businessException = new BusinessException();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            businessException.addKey("Please log in to perform this action");
            throw businessException;
        }

        if (!(authentication.getPrincipal() instanceof User authenticatedUser)) {
            businessException.addKey("An unexpected error occurred. Please try again");
            throw businessException;
        }

        if (!(authenticatedUser.getUserId().equals(item.getSeller().getUserId()))) {
            businessException.addKey("You do not have permission to perform this action");
            throw businessException;
        }
    }



    @Override
    public void removeItemById(int itemId) {
        itemDao.deleteById(itemId);
    }

    @Override
    public void handleItemDeletion(int itemId, Authentication authentication) throws BusinessException {
        BusinessException bException = new BusinessException();

        try {
            Optional<Item> optionalItem = findItemById(itemId);

            if (optionalItem.isPresent()) {
                Item item = optionalItem.get();

                System.out.println(itemId);
                isAuctionstarted(item);
                hasAuctionEnded(item);
                isUserAuthorized(item, authentication);

                removeItemById(itemId);
            }

        } catch (BusinessException businessException) {
            throw businessException;
        } catch (Exception e) {
            bException.addKey("An unexpected error occurred during auction creation: " + e.getMessage());
            throw bException;
        }
    }

    @Override
    public List<Item> getAllItems() {
        return itemDao.findAll();
    }

    @Override
    public List<Item> searchItems(String query, List<Integer> categories, int offset, int limit) {
        List<Item> items =  itemDao.searchItems(query, categories, offset, limit);
        items.forEach(item -> item.setSaleStatus(calculateItemStatus(item)));
        return items;
    }

    @Override
    public List<Item> getAllPaginated(int page, int size) {
        List<Item> items = itemDao.findAllPaginated(page, size);
        items.forEach(item -> item.setSaleStatus(calculateItemStatus(item)));
        return items;
    }

    @Override
    public List<Item> getByUserIdPaginated(int userId, int page, int size) {
        List<Item> items = itemDao.findAllByUserIdPaginated(userId, page, size);
        items.forEach(item -> item.setSaleStatus(calculateItemStatus(item)));
        return items;
    }

    @Override
    public int getCountOfItemsByUserId(int userId) {
        return itemDao.countByUserId(userId);
    }

    @Override
    public int getTotalItemCount() {
        return itemDao.count();
    }

    @Override
    public int getTotalItemCountByCategory(int categoryId) {
        return itemDao.countByCategoryId(categoryId);
    }

    @Override
    public int countFilteredItems(String query, List<Integer> categories) {
        return itemDao.countFilteredItems(query, categories);
    }

    @Override
    public int getTotalPageCount(int size) {
        int totalItemCount = getTotalItemCount();
        return (int) Math.ceil((double) totalItemCount / size);
    }

    @Override
    public Item updateItemAfterAuction(Auction auction, int itemId) throws BusinessException {
        BusinessException businessException = new BusinessException();

        Optional<Item> optionalItem = findItemById(itemId);
        if (optionalItem.isEmpty()) {
            businessException.addKey("Item not found, cannot create auction!");
            throw businessException;
        }

        Item item = optionalItem.get();
        item.setSellingPrice(auction.getBidAmount());

        updateItem(item);
        return item;
    }

    private String calculateItemStatus(Item item) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(item.getAuctionStartingDate())) {
            return "pending";
        } else if (now.isAfter(item.getAuctionEndingDate())) {
            return "concluded";
        } else {
            return "active";
        }
    }

    private boolean isItemNameValid(String itemName, BusinessException businessException) {
        if (itemName == null) {
            businessException.addKey("BusinessCode.VALIDATION_ITEM_NAME_NULL");
            return false;
        }

        if (itemName.length() > 30) {
            businessException.addKey("BusinessCode.VALIDATION_ITEM_NAME_SIZE");
            return false;
        }

        return true;
    }

    private boolean isDescriptionValid(String description, BusinessException businessException) {
        if (description == null) {
            businessException.addKey("BusinessCode.VALIDATION_DESCRIPTION_NULL");
            return false;
        }

        if (description.length() > 300) {
            businessException.addKey("BusinessCode.VALIDATION_DESCRIPTION_SIZE");
            return false;
        }

        return true;
    }

    private boolean isAuctionStartingDateValid(LocalDateTime auctionStartingDate, BusinessException businessException) {
        if (auctionStartingDate == null) {
            businessException.addKey("BusinessCode.VALIDATION_AUCTION_STARTING_DATE_NULL");
            return false;
        }
        return true;
    }

    private boolean isAuctionEndingDateValid(LocalDateTime auctionStartingDate, LocalDateTime auctionEndingDate, BusinessException businessException) {
        if (auctionEndingDate == null) {
            businessException.addKey("BusinessCode.VALIDATION_AUCTION_ENDING_DATE_NULL");
            return false;
        }

        if (auctionEndingDate.isBefore(auctionStartingDate)) {
            businessException.addKey("BusinessCode.VALIDATION_AUCTION_ENDING_DATE_AFTER_AUCTION_STARTING_DATE");
            return false;
        }
        return true;
    }

    private boolean isCategoryValid(Category category, BusinessException businessException) {
        if (category == null) {
            businessException.addKey("BusinessCode.VALIDATION_CATEGORY_NULL");
            return false;
        }

        return true;
    }
}
