package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.dto.BidHistoryDto;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface AuctionService {
    void createAuction(Auction auction);

    Optional<Auction> getAuctionById(int auctionId);

    Optional<Auction> getTopByItemIdOrderByAuctionDateDesc(int itemId);

    List<Auction> getAuctionsByItemIdPaginated(int itemId, int page, int size);

    List<Auction> getAuctionsByUserIdPaginated(int userId, int page, int size);

    List<Auction> getAllAuctionsByItemId(int itemId);

    void removeAuctionById(int auctionId);

    int getTotalAuctionCount();

    List<BidHistoryDto> getItemBidHistoryPaginated(int itemId, int page, int size);

    int getCountOfAuctionsByItemId(int itemId);

    int getCountOfAuctionsByItemIdAndUserId(int itemId, int userId);

    void handleAuctionCreation(Auction auction, Authentication authentication) throws BusinessException;
}

