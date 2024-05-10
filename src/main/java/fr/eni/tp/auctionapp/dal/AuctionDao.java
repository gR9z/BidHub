package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.dto.BidHistoryDto;

import java.util.List;
import java.util.Optional;

public interface AuctionDao {
    void insert(Auction auction);
    Optional<Auction> findByAuctionId(int auctionId);
    Optional<Auction> findTopByItemIdOrderByAuctionDateDesc(int itemId);
    List<Auction> findAllAuctionsByItemId(int itemId);
    List<Auction> findAuctionsByItemIdPaginated(int itemId, int page, int size);
    List<Auction> findAuctionsByUserIdPaginated(int userId, int page, int size);
    List<BidHistoryDto> findBidHistoryForItemPaginated(int itemId, int page, int size);
    int countByItemId(int itemId);
    int countByItemIdAndUserId(int itemId, int userId);
    void deleteById(int auctionId);
    int count();
}
