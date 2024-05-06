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
    void test_createAuctionWithAuctionDate() {
        LocalDateTime auctionDate = LocalDateTime.of(2024, 5, 15, 17, 45);
        auction.setAuctionDate(auctionDate);

        auctionService.createAuction(auction);

        Optional<Auction> optionalAuction = auctionService.findAuctionById(auction.getAuctionId());
        assertThat(optionalAuction.isPresent()).isTrue();
        Auction getAuction = optionalAuction.get();

        assertThat(getAuction.getAuctionDate()).isEqualTo(auctionDate);
    }

    @Test
    void test_findAuctionById() {
        auctionService.createAuction(auction);
        Optional<Auction> optionalAuction = auctionService.findAuctionById(auction.getAuctionId());
        assertThat(optionalAuction.isPresent()).isTrue();
        Auction getAuction = optionalAuction.get();
        assertThat(getAuction.getAuctionId()).isEqualTo(auction.getAuctionId());
    }

    @Test
    void test_findAuctionsByItemIdPaginated() {
        for (int i = 0; i < 25; i++) {
            Auction auction = testDatabaseService.createAuction(user, item);
            testDatabaseService.insertAuctionInDatabase(auction);
        }

        List<Optional<Auction>> optionalAuctions = auctionService.findAuctionsByItemIdPaginated(item.getItemId(), 1, 10);
        List<Auction> auctions = optionalAuctions.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        assertThat(auctions.size()).isEqualTo(10);
    }

    @Test void test_findAuctionsByUserIdPaginated() {
        User user2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());

        for (int i = 0; i < 25; i++) {
            Auction auction = testDatabaseService.createAuction(user, item);
            testDatabaseService.insertAuctionInDatabase(auction);
        }

        for (int i = 0; i < 15; i++) {
            Auction auction = testDatabaseService.createAuction(user2, item);
            testDatabaseService.insertAuctionInDatabase(auction);
        }

        List<Optional<Auction>> optionalAuctionsFromUser = auctionService.findAuctionsByUserIdPaginated(user.getUserId(), 1, 5);
        List<Optional<Auction>> optionalAuctionsFromUser2 = auctionService.findAuctionsByUserIdPaginated(user2.getUserId(), 1, 10);

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
    void test_removeAuctionById() {
        auctionService.createAuction(auction);
        Optional<Auction> optionalAuction = auctionService.findAuctionById(auction.getAuctionId());
        assertThat(optionalAuction.isPresent()).isTrue();

        auctionService.removeAuctionById(auction.getAuctionId());
        Optional<Auction> optionalAuction2 = auctionService.findAuctionById(auction.getAuctionId());
        assertThat(optionalAuction2.isPresent()).isFalse();
    }

    @Test
    void test_getTotalAuctionCount() {
        User user2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        Item item2 = testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(user2, category));

        for (int i = 0; i < 7; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user, item));
        }

        for (int i = 0; i < 9; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user2, item2));
        }

        int auctions = auctionService.getTotalAuctionCount();
        assertThat(auctions).isEqualTo(7 + 9);
    }

    @Test
    void test_getCountOfAuctionsByItemId() {
        User user2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        Item item2 = testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(user2, category));

        for (int i = 0; i < 7; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user, item));
        }

        for (int i = 0; i < 9; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user2, item2));
        }

        int auctions = auctionService.getCountOfAuctionsByItemId(item.getItemId());
        int auctions2 = auctionService.getCountOfAuctionsByItemId(item2.getItemId());
        assertThat(auctions).isEqualTo(7);
        assertThat(auctions2).isEqualTo(9);
    }

    @Test
    void test_getCountOfAuctionsByItemIdAndUserId() {
        User user2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());

        for (int i = 0; i < 12; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user, item));
        }

        for (int i = 0; i < 8; i++) {
            testDatabaseService.insertAuctionInDatabase(testDatabaseService.createAuction(user2, item));
        }

        int auctions = auctionService.getCountOfAuctionsByItemIdAndUserId(item.getItemId(), user.getUserId());
        int auctions2 = auctionService.getCountOfAuctionsByItemIdAndUserId(item.getItemId(), user2.getUserId());

        assertThat(auctions).isEqualTo(12);
        assertThat(auctions2).isEqualTo(8);
    }
}