package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.AuctionService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.dal.AuctionDao;
import fr.eni.tp.auctionapp.dal.ItemDao;
import fr.eni.tp.auctionapp.dal.UserDao;
import fr.eni.tp.auctionapp.dto.BidHistoryDto;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionServiceImpl implements AuctionService {

    AuctionDao auctionDao;
    ItemDao itemDao;
    UserDao userDao;
    ItemService itemService;
    UserService userService;

    public AuctionServiceImpl(AuctionDao auctionDao, ItemDao itemDao, UserDao userDao, ItemService itemService, UserService userService) {
        this.auctionDao = auctionDao;
        this.itemDao = itemDao;
        this.userDao = userDao;

        this.itemService = itemService;
        this.userService = userService;
    }

    @Override
    public void createAuction(Auction auction) {
        auctionDao.insert(auction);
    }

    @Override
    @Transactional
    public void handleAuctionCreation(Auction auction, Authentication authentication) throws BusinessException {
        BusinessException bException = new BusinessException();
        try {
            Item item = itemService.updateItemAfterAuction(auction, auction.getItemId());

            validateAuctionTiming(item);

            Optional<Auction> lastAuction = validateAuctionAmount(item.getItemId(), auction);
            lastAuction.ifPresent(la -> {
                userService.refundUser(la.getUserId(), la.getBidAmount());
            });

            userService.debitUserCredit(authentication, auction);

            auctionDao.insert(auction);

        } catch (BusinessException businessException) {
            throw businessException;
        } catch (Exception e) {
            bException.addKey("An unexpected error occurred during auction creation: " + e.getMessage());
            throw bException;
        }
    }

    private void validateAuctionTiming(Item item) throws BusinessException {
        BusinessException businessException = new BusinessException();

        if (item.getAuctionEndingDate().isBefore(LocalDateTime.now())) {
            businessException.addKey("Bidding is no longer possible for this item as the auction has already ended.");
            throw businessException;
        }

        if (item.getAuctionStartingDate().isAfter(LocalDateTime.now())) {
            businessException.addKey("Bidding is not yet possible for this item as the auction has not started.");
            throw businessException;
        }
    }

    private Optional<Auction> validateAuctionAmount(int itemId, Auction auction) throws BusinessException {
        BusinessException businessException = new BusinessException();

        Optional<Auction> optionalLastAuction = auctionDao.findTopByItemIdOrderByAuctionDateDesc(itemId);
        optionalLastAuction.ifPresent(lastAuction -> {
            int minimumBidAmount = (int) Math.ceil(lastAuction.getBidAmount() * 1.10);
            if (auction.getBidAmount() < minimumBidAmount) {
                businessException.addKey("Your bid must be at least 10% higher than the current bid of €" + lastAuction.getBidAmount() + ", which is €" + minimumBidAmount + ".");
                throw businessException;
            }
        });

        return optionalLastAuction;
    }

    @Override
    public Optional<Auction> getAuctionById(int auctionId) {
        return auctionDao.findByAuctionId(auctionId);
    }

    @Override
    public Optional<Auction> getTopByItemIdOrderByAuctionDateDesc(int itemId) {
        return auctionDao.findTopByItemIdOrderByAuctionDateDesc(itemId);
    }

    @Override
    public List<Auction> getAuctionsByItemIdPaginated(int itemId, int page, int size) {
        return auctionDao.findAuctionsByItemIdPaginated(itemId, page, size);
    }

    @Override
    public List<Auction> getAuctionsByUserIdPaginated(int userId, int page, int size) {
        return auctionDao.findAuctionsByUserIdPaginated(userId, page, size);
    }

    @Override
    public List<Auction> getAllAuctionsByItemId(int itemId) {
        return auctionDao.findAllAuctionsByItemId(itemId);
    }

    @Override
    public List<BidHistoryDto> getItemBidHistoryPaginated(int itemId, int page, int size) {
        return auctionDao.findBidHistoryForItemPaginated(itemId, page, size);
    }

    @Override
    public void removeAuctionById(int auctionId) {
        auctionDao.deleteById(auctionId);
    }

    @Override
    public int getTotalAuctionCount() {
        return auctionDao.count();
    }

    @Override
    public int getCountOfAuctionsByItemId(int itemId) {
        return auctionDao.countByItemId(itemId);
    }

    @Override
    public int getCountOfAuctionsByItemIdAndUserId(int itemId, int userId) {
        return auctionDao.countByItemIdAndUserId(itemId, userId);
    }
}
