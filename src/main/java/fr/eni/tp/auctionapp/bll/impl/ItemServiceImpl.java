package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.bo.Withdrawal;
import fr.eni.tp.auctionapp.dal.ItemDao;
import fr.eni.tp.auctionapp.dal.WithdrawalDao;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;
    private WithdrawalDao withdrawalDao;

    public ItemServiceImpl(ItemDao itemDao, WithdrawalDao withdrawalDao) {
        this.itemDao = itemDao;
        this.withdrawalDao = withdrawalDao;
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
        return itemDao.findById(id);
    }

    @Override
    public void updateItem(Item item) {
        itemDao.update(item);
    }

    @Override
    public void removeItemById(int itemId) {
        itemDao.deleteById(itemId);
    }

    @Override
    public List<Item> getAllItems() {
        return itemDao.findAll();
    }

    @Override
    public List<Item> getAllPaginated(int page, int size) {
        return itemDao.findAllPaginated(page, size);
    }

    @Override
    public List<Item> getByUserIdPaginated(int userId, int page, int size) {
        return itemDao.findAllByUserIdPaginated(userId, page, size);
    }

    @Override
    public int getCountOfItemsByUserId(int userId) {
        return itemDao.countByUserId(userId);
    }

    @Override
    public int getTotalItemCount() {
        return itemDao.count();
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
