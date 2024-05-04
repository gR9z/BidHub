package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.TestDatabaseService;
import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestAuctionDaoServiceImpl {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private TestDatabaseService testDatabaseService;

    User user;
    Category category;
    Item item;
    Auction auction = new Auction();

    @BeforeEach
    public void setup() {
        testDatabaseService.clearDatabase();

        user = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        category = testDatabaseService.insertCategoryInDatabase(testDatabaseService.createRandomCategory());
        item = testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(user, category));

        auction.setItemId(item.getItemId());
        auction.setUserId(user.getUserId());
    }

    @Test
    void test_insertAuctionWithAuctionDate() {
        LocalDateTime auctionDate = LocalDateTime.of(2024, 5, 15, 17, 45);
        auction.setAuctionDate(auctionDate);

        auctionService.insert(auction);

        Optional<Auction> optionalAuction = auctionService.readByAuctionId(auction.getAuctionId());
        assertThat(optionalAuction.isPresent()).isTrue();
        Auction getAuction = optionalAuction.get();

        assertThat(getAuction.getAuctionDate()).isEqualTo(auctionDate);
    }

    @Test
    void test_readByAuctionId() {
        auctionService.insert(auction);
        Optional<Auction> optionalAuction = auctionService.readByAuctionId(auction.getAuctionId());
        assertThat(optionalAuction.isPresent()).isTrue();
        Auction getAuction = optionalAuction.get();
        assertThat(getAuction.getAuctionId()).isEqualTo(auction.getAuctionId());
    }

    @Test
    void test_readByItemIdPagination() {
        for (int i = 0; i < 25; i++) {
            Auction auction = testDatabaseService.createAuction(user, item);
            testDatabaseService.insertAuctionInDatabase(auction);
        }

        List<Optional<Auction>> optionalAuctions = auctionService.readByItemIdPaginated(item.getItemId(), 1, 10);
        List<Auction> auctions = optionalAuctions.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        assertThat(auctions.size()).isEqualTo(10);
    }

    @Test void test_readByUserIdPaginated() {
        User user2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());

        for (int i = 0; i < 25; i++) {
            Auction auction = testDatabaseService.createAuction(user, item);
            testDatabaseService.insertAuctionInDatabase(auction);
        }

        for (int i = 0; i < 15; i++) {
            Auction auction = testDatabaseService.createAuction(user2, item);
            testDatabaseService.insertAuctionInDatabase(auction);
        }

        List<Optional<Auction>> optionalAuctionsFromUser = auctionService.readByItemIdPaginated(item.getItemId(), 1, 5);
        List<Optional<Auction>> optionalAuctionsFromUser2 = auctionService.readByItemIdPaginated(item.getItemId(), 1, 10);

        List<Auction> auctionsFromUser = optionalAuctionsFromUser.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        List<Auction> auctionsFromUser2 = optionalAuctionsFromUser2.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        assertThat(auctionsFromUser.size()).isEqualTo(5);
        assertThat(auctionsFromUser2.size()).isEqualTo(10);
    }

    @Test
    void test_deleteByAuctionId() {
        auctionService.insert(auction);
        Optional<Auction> optionalAuction = auctionService.readByAuctionId(auction.getAuctionId());
        assertThat(optionalAuction.isPresent()).isTrue();

        auctionService.deleteByAuctionId(auction.getAuctionId());
        Optional<Auction> optionalAuction2 = auctionService.readByAuctionId(auction.getAuctionId());
        assertThat(optionalAuction2.isPresent()).isFalse();
    }

    @Test
    void test_count() {
        User user2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        Item item2 = testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(user2, category));

        for (int i = 0; i < 7; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user, item));
        }

        for (int i = 0; i < 9; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user2, item2));
        }

        int auctions = auctionService.count();
        assertThat(auctions).isEqualTo(7 + 9);
    }

    @Test
    void test_countByItemId() {
        User user2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        Item item2 = testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(user2, category));

        for (int i = 0; i < 7; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user, item));
        }

        for (int i = 0; i < 9; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user2, item2));
        }

        int auctions = auctionService.countByItemId(item.getItemId());
        int auctions2 = auctionService.countByItemId(item2.getItemId());
        assertThat(auctions).isEqualTo(7);
        assertThat(auctions2).isEqualTo(9);
    }

    @Test
    void test_countByItemIdAndUserId() {
        User user2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());

        for (int i = 0; i < 12; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user, item));
        }

        for (int i = 0; i < 8; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user2, item));
        }

        int auctions = auctionService.countByItemIdAndUserId(item.getItemId(), user.getUserId());
        int auctions2 = auctionService.countByItemIdAndUserId(item.getItemId(), user2.getUserId());

        assertThat(auctions).isEqualTo(12);
        assertThat(auctions2).isEqualTo(8);
    }
}