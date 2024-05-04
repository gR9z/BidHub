package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.AuctionService;
import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.dal.AuctionDao;
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
    public void insert(Auction auction) {
        auctionDao.insert(auction);
    }

    @Override
    public Optional<Auction> readByAuctionId(int auctionId) {
        return auctionDao.readByAuctionId(auctionId);
    }

    @Override
    public List<Optional<Auction>> readByItemIdPaginated(int itemId, int page, int size) {
        return auctionDao.readByItemIdPaginated(itemId, page, size);
    }

    @Override
    public List<Optional<Auction>> readByUserIdPaginated(int userId, int page, int size) {
        return auctionDao.readByUserIdPaginated(userId, page, size);
    }

    @Override
    public void deleteByAuctionId(int auctionId) {
        auctionDao.deleteByAuctionId(auctionId);
    }

    @Override
    public int count() {
        return auctionDao.count();
    }

    @Override
    public int countByItemId(int itemId) {
        return auctionDao.countByItemId(itemId);
    }

    @Override
    public int countByItemIdAndUserId(int itemId, int userId) {
        return auctionDao.countByItemIdAndUserId(itemId, userId);
    }
}
