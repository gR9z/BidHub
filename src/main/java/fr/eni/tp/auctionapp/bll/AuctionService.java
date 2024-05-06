package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.Auction;

import java.util.List;
import java.util.Optional;

public interface AuctionService {
    void createAuction(Auction auction);
    Optional<Auction> findAuctionById(int auctionId);
    List<Optional<Auction>> findAuctionsByItemIdPaginated(int itemId, int page, int size);
    List<Optional<Auction>> findAuctionsByUserIdPaginated(int userId, int page, int size);
    void removeAuctionById(int auctionId);
    int getTotalAuctionCount();
    int getCountOfAuctionsByItemId(int itemId);
    int getCountOfAuctionsByItemIdAndUserId(int itemId, int userId);
}

