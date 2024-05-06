package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.Auction;

import java.util.List;
import java.util.Optional;

public interface AuctionDao {
    void insert(Auction auction);
    Optional<Auction> findByAuctionId(int auctionId);
    List<Optional<Auction>> findAuctionsByItemIdPaginated(int itemId, int page, int size);
    List<Optional<Auction>> findAuctionsByUserIdPaginated(int userId, int page, int size);
    int countByItemId(int itemId);
    int countByItemIdAndUserId(int itemId, int userId);
    void deleteById(int auctionId);
    int count();
}
