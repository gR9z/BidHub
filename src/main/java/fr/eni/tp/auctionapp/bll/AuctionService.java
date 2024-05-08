package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.dto.BidHistoryDto;

import java.util.List;
import java.util.Optional;

public interface AuctionService {
    void createAuction(Auction auction);
    Optional<Auction> findAuctionById(int auctionId);
    List<Auction> findAuctionsByItemIdPaginated(int itemId, int page, int size);
    List<Auction> findAuctionsByUserIdPaginated(int userId, int page, int size);
    void removeAuctionById(int auctionId);
    int getTotalAuctionCount();
    List<BidHistoryDto> getItemBidHistoryPaginated(int itemId, int page, int size);
    int getCountOfAuctionsByItemId(int itemId);
    int getCountOfAuctionsByItemIdAndUserId(int itemId, int userId);
}

