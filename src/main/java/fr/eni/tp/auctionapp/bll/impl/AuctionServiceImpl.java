package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.AuctionService;
import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.dal.AuctionDao;
import fr.eni.tp.auctionapp.dto.BidHistoryDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuctionServiceImpl implements AuctionService {

    AuctionDao auctionDao;

    public AuctionServiceImpl(AuctionDao auctionDao) {
        this.auctionDao = auctionDao;
    }

    @Override
    public void createAuction(Auction auction) {
        auctionDao.insert(auction);
    }

    @Override
    public Optional<Auction> findAuctionById(int auctionId) {
        return auctionDao.findByAuctionId(auctionId);
    }

    @Override
    public List<Auction> findAuctionsByItemIdPaginated(int itemId, int page, int size) {
        return auctionDao.findAuctionsByItemIdPaginated(itemId, page, size);
    }

    @Override
    public List<Auction> findAuctionsByUserIdPaginated(int userId, int page, int size) {
        return auctionDao.findAuctionsByUserIdPaginated(userId, page, size);
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
    public List<BidHistoryDto> getItemBidHistoryPaginated(int itemId, int page, int size) {
        return auctionDao.findBidHistoryForItemPaginated(itemId, page, size);
    }

    @Override
    public int getCountOfAuctionsByItemIdAndUserId(int itemId, int userId) {
        return auctionDao.countByItemIdAndUserId(itemId, userId);
    }
}
