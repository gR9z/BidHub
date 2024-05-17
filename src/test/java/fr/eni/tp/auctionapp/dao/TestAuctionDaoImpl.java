package fr.eni.tp.auctionapp.dao;

import fr.eni.tp.auctionapp.TestDatabaseService;
import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.AuctionDao;
import fr.eni.tp.auctionapp.dal.UserDao;
import fr.eni.tp.auctionapp.dto.BidHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestAuctionDaoImpl {

    @Autowired
    private AuctionDao auctionDao;

    @Autowired
    private TestDatabaseService testDatabaseService;

    @Autowired
    private UserDao userDao;

    private User user;
    private Category category;
    private Item item;
    private final Auction auction = new Auction();
    private int initialPrice;
    Random random = new Random();

    @BeforeEach
    public void setup() {
        testDatabaseService.clearDatabase();

        user = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        category = testDatabaseService.insertCategoryInDatabase(testDatabaseService.createRandomCategory());
        item = testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(user, category));

        auction.setItemId(item.getItemId());
        auction.setUserId(user.getUserId());

        initialPrice = item.getStartingPrice();
    }

    @Test
    void test_insert() {
        LocalDateTime auctionDate = LocalDateTime.of(2024, 5, 15, 17, 45);
        auction.setAuctionDate(auctionDate);
        auctionDao.insert(auction);

        Optional<Auction> optionalAuction = auctionDao.findByAuctionId(auction.getAuctionId());
        assertThat(optionalAuction.isPresent()).isTrue();
        Auction getAuction = optionalAuction.get();

        assertThat(getAuction.getAuctionDate()).isEqualTo(auctionDate);
    }

    @Test
    void test_findByAuctionId() {
        auctionDao.insert(auction);
        Optional<Auction> optionalAuction = auctionDao.findByAuctionId(auction.getAuctionId());
        assertThat(optionalAuction.isPresent()).isTrue();
        Auction getAuction = optionalAuction.get();
        assertThat(getAuction.getAuctionId()).isEqualTo(auction.getAuctionId());
    }

    @Test
    void test_findAuctionsByItemIdPaginated() {
        for (int i = 0; i < 25; i++) {
            Auction auction = testDatabaseService.createAuction(user, item, initialPrice);
            testDatabaseService.insertAuctionInDatabase(auction);
            initialPrice = auction.getBidAmount() + random.nextInt(100);
        }

        List<Auction> auctions = auctionDao.findAuctionsByItemIdPaginated(item.getItemId(), 1, 10);
        assertThat(auctions.size()).isEqualTo(10);
    }

    @Test
    void test_findAuctionsByUserIdPaginated() {
        User user2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());

        for (int i = 0; i < 25; i++) {
            Auction auction = testDatabaseService.createAuction(user, item, initialPrice);
            testDatabaseService.insertAuctionInDatabase(auction);
            initialPrice = auction.getBidAmount() + random.nextInt(150);
        }

        for (int i = 0; i < 15; i++) {
            Auction auction = testDatabaseService.createAuction(user2, item, initialPrice);
            testDatabaseService.insertAuctionInDatabase(auction);
            initialPrice = auction.getBidAmount() + random.nextInt(150);
        }

        List<Auction> auctionsFromUser = auctionDao.findAuctionsByUserIdPaginated(user.getUserId(), 1, 5);
        List<Auction> auctionsFromUser2 = auctionDao.findAuctionsByUserIdPaginated(user2.getUserId(), 1, 10);

        assertThat(auctionsFromUser.size()).isEqualTo(5);
        assertThat(auctionsFromUser2.size()).isEqualTo(10);
    }

    @Test
    public void test_findBidHistoryForItemPaginated() {
        int numberOfBids = 20;
        for (int i = 0; i < numberOfBids; i++) {
            Auction auction = testDatabaseService.createAuction(user, item, initialPrice);
            testDatabaseService.insertAuctionInDatabase(auction);
            initialPrice = auction.getBidAmount() + random.nextInt(150);
        }

        int page = 1;
        int size = 10;
        List<BidHistoryDto> bidHistory = auctionDao.findBidHistoryForItemPaginated(item.getItemId(), page, size);

        for (BidHistoryDto bid : bidHistory) {
            assertThat(bid.getTotalCount()).isEqualTo(numberOfBids);
            assertThat(bid).isNotNull();
            assertThat(bid.getAuctionDate()).isNotNull();
            assertThat(bid.getBidAmount()).isNotNull();
        }

        assertThat(bidHistory).isNotNull();
        assertThat(bidHistory).hasSize(size);
    }

    @Test
    void test_deleteById() {
        auctionDao.insert(auction);
        Optional<Auction> optionalAuction = auctionDao.findByAuctionId(auction.getAuctionId());
        assertThat(optionalAuction.isPresent()).isTrue();

        auctionDao.deleteById(auction.getAuctionId());
        Optional<Auction> optionalAuction2 = auctionDao.findByAuctionId(auction.getAuctionId());
        assertThat(optionalAuction2.isPresent()).isFalse();
    }

    @Test
    void test_count() {
        User user2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        Item item2 = testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(user2, category));
        int initialPrice2 = item2.getStartingPrice();

        for (int i = 0; i < 7; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user, item, initialPrice));
            initialPrice = auction.getBidAmount() + random.nextInt(150);
        }

        for (int i = 0; i < 9; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user2, item2, initialPrice2));
            initialPrice2 = auction.getBidAmount() + random.nextInt(150);
        }

        int auctions = auctionDao.count();
        assertThat(auctions).isEqualTo(7 + 9);
    }

    @Test
    void test_countByItemId() {
        User user2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        Item item2 = testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(user2, category));
        int initialPrice2 = item2.getStartingPrice();

        for (int i = 0; i < 7; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user, item, initialPrice));
            initialPrice = auction.getBidAmount() + random.nextInt(150);
        }

        for (int i = 0; i < 9; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user2, item2, initialPrice2));
            initialPrice2 = auction.getBidAmount() + random.nextInt(150);
        }

        int auctions = auctionDao.countByItemId(item.getItemId());
        int auctions2 = auctionDao.countByItemId(item2.getItemId());
        assertThat(auctions).isEqualTo(7);
        assertThat(auctions2).isEqualTo(9);
    }

    @Test
    void test_countByItemIdAndUserId() {
        User user2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        initialPrice = auction.getBidAmount() + random.nextInt(150);

        for (int i = 0; i < 12; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user, item, initialPrice));
        }

        for (int i = 0; i < 8; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user2, item, initialPrice));
        }

        int auctions = auctionDao.countByItemIdAndUserId(item.getItemId(), user.getUserId());
        int auctions2 = auctionDao.countByItemIdAndUserId(item.getItemId(), user2.getUserId());

        assertThat(auctions).isEqualTo(12);
        assertThat(auctions2).isEqualTo(8);
    }
}