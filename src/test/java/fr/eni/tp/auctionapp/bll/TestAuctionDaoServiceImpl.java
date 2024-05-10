package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bll.impl.AuctionServiceImpl;
import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.dal.AuctionDao;
import fr.eni.tp.auctionapp.dto.BidHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TestAuctionDaoServiceImpl {

    @Mock
    private AuctionDao auctionDaoMock;

    @InjectMocks
    private AuctionServiceImpl auctionService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_createAuctionWithAuctionDate() {
        Auction auction = new Auction();

        doNothing().when(auctionDaoMock).insert(auction);

        auctionService.createAuction(auction);

        verify(auctionDaoMock, times(1)).insert(auction);
    }

    @Test
    void test_findAuctionById() {
        Auction auction = new Auction();
        int auctionId = 123;

        when(auctionDaoMock.findByAuctionId(auctionId)).thenReturn(Optional.of(auction));

        Optional<Auction> result = auctionService.getAuctionById(auctionId);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(auction);

        verify(auctionDaoMock).findByAuctionId(auctionId);
    }

    @Test
    void test_findAuctionsByItemIdPaginated() {
        List<Auction> auctions = new ArrayList<>();
        auctions.add(new Auction());
        auctions.add(new Auction());

        when(auctionDaoMock.findAuctionsByItemIdPaginated(anyInt(), anyInt(), anyInt())).thenReturn(auctions);
        List<Auction> result = auctionService.getAuctionsByItemIdPaginated(123, 1, 10);
        assertThat(result).isNotNull();
        verify(auctionDaoMock).findAuctionsByItemIdPaginated(123, 1, 10);
    }

    @Test
    void test_findAuctionsByUserIdPaginated() {
        List<Auction> auctions = new ArrayList<>();
        auctions.add(new Auction());
        auctions.add(new Auction());

        List<Auction> auctionList = auctions.stream()
                .toList();

        when(auctionDaoMock.findAuctionsByUserIdPaginated(anyInt(), anyInt(), anyInt())).thenReturn(auctionList);
        List<Auction> result = auctionService.getAuctionsByUserIdPaginated(123, 1, 10);
        assertThat(result).isNotNull();
        verify(auctionDaoMock).findAuctionsByUserIdPaginated(123, 1, 10);
    }

    @Test
    void test_getItemBidHistoryPaginated() {
        int itemId = 123;
        int page = 1;
        int size = 10;
        List<BidHistoryDto> bidHistory = new ArrayList<>();
        bidHistory.add(new BidHistoryDto());
        bidHistory.add(new BidHistoryDto());

        when(auctionDaoMock.findBidHistoryForItemPaginated(itemId, page, size)).thenReturn(bidHistory);
        List<BidHistoryDto> result = auctionService.getItemBidHistoryPaginated(itemId, page, size);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        verify(auctionDaoMock).findBidHistoryForItemPaginated(itemId, page, size);
    }

    @Test
    void test_removeAuctionById() {
        int auctionId = 123;
        auctionService.removeAuctionById(auctionId);
        verify(auctionDaoMock).deleteById(auctionId);
    }

    @Test
    void test_getTotalAuctionCount() {
        int count = 10;
        when(auctionDaoMock.count()).thenReturn(count);
        int result = auctionService.getTotalAuctionCount();
        assertThat(result).isEqualTo(count);
        verify(auctionDaoMock).count();
    }

    @Test
    void test_getCountOfAuctionsByItemId() {
        int itemId = 123;
        int count = 5;
        when(auctionDaoMock.countByItemId(itemId)).thenReturn(count);
        int result = auctionService.getCountOfAuctionsByItemId(itemId);
        assertThat(result).isEqualTo(count);
        verify(auctionDaoMock).countByItemId(itemId);
    }

    @Test
    void test_getCountOfAuctionsByItemIdAndUserId() {
        int itemId = 123;
        int userId = 456;
        int count = 8;

        when(auctionDaoMock.countByItemIdAndUserId(itemId, userId)).thenReturn(count);
        int result = auctionService.getCountOfAuctionsByItemIdAndUserId(itemId, userId);
        assertThat(result).isEqualTo(count);
        verify(auctionDaoMock).countByItemIdAndUserId(itemId, userId);
    }
}
