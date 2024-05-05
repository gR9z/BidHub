package fr.eni.tp.auctionapp.bll;

import com.github.javafaker.Faker;
import fr.eni.tp.auctionapp.TestDatabaseService;
import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestItemServiceImpl {

    @Autowired
    TestDatabaseService testDatabaseService;

    @Autowired
    private ItemService itemService;

    private final Faker faker = new Faker();

    private User seller;
    private Category category;
    private Item item;

    @BeforeEach
    public void setup() {
        testDatabaseService.clearDatabase();
        seller = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        category = testDatabaseService.insertCategoryInDatabase(testDatabaseService.createRandomCategory());
        item = testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller, category));
    }

    @Test
    void test_createItem() {
        Item item2 = new Item();

        item2.setItemName(faker.commerce().productName());
        item2.setDescription(faker.lorem().sentence());
        item2.setAuctionStartingDate(LocalDateTime.now());
        item2.setAuctionEndingDate(LocalDateTime.now().plusDays(7));

        int startingPrice = faker.number().numberBetween(100, 1000);
        item2.setStartingPrice(startingPrice);
        item2.setSellingPrice(startingPrice + faker.number().numberBetween(1, 500));
        item2.setImageUrl(faker.internet().url());

        item2.setSeller(seller);
        item2.setCategory(category);

        itemService.createItem(item2);

        Optional<Item> optionalItem = itemService.findItemById(item2.getItemId());
        assertThat(optionalItem.isPresent()).isTrue();
        Item itemRead = optionalItem.get();

        assertThat(itemRead.getItemId()).isEqualTo(item2.getItemId());
        assertThat(itemRead.getItemName()).isEqualTo(item2.getItemName());
        assertThat(itemRead.getDescription()).isEqualTo(item2.getDescription());
    }

    @Test
    void test_findItemById() {
        Optional<Item> optionalItem = itemService.findItemById(item.getItemId());
        assertThat(optionalItem.isPresent()).isTrue();
        Item getItem = optionalItem.get();
        assertThat(item.getItemId()).isEqualTo(getItem.getItemId());
        assertThat(item.getItemName()).isEqualTo(getItem.getItemName());
    }

    @Test
    void test_updateItem() {
        item.setImageUrl("/images/img.jpg");
        itemService.updateItem(item);
        Optional<Item> optionalItem = itemService.findItemById(item.getItemId());
        assertThat(optionalItem.isPresent()).isTrue();
        Item getItem = optionalItem.get();
        assertThat(item.getItemId()).isEqualTo(getItem.getItemId());
        assertThat(item.getImageUrl()).isEqualTo(getItem.getImageUrl()).isEqualTo("/images/img.jpg");
    }

    @Test
    void test_removeItemById() {
        itemService.removeItemById(item.getItemId());
        Optional<Item> optionalItem = itemService.findItemById(item.getItemId());
        assertThat(optionalItem.isPresent()).isFalse();
    }

    @Test
    void test_getAllItems() {
        for (int i = 0; i < 19; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller, category));
        }
        List<Item> allItems = itemService.getAllItems();

        assertThat(allItems.size()).isEqualTo(19 + 1);
        // +1 because 1 item is created and inserted into the database in the @BeforeEach

    }

    @Test
    void test_getAllPaginated() {
        for (int i = 0; i < 25; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller, category));
        }
        List<Item> allItems = itemService.getAllPaginated(1, 15);
        assertThat(allItems.size()).isEqualTo(15);
    }

    @Test
    void test_getByUserIdPaginated() {
        User seller2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        for (int i = 0; i < 15; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller, category));
        }

        for (int i = 0; i < 15; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller2, category));
        }

        List<Item> allItemsByUserId = itemService.getByUserIdPaginated(seller.getUserId(), 1, 6);
        assertThat(allItemsByUserId.size()).isEqualTo(6);

        for (Item itemByUserId : allItemsByUserId) {
            assertThat(itemByUserId.getSeller().getUserId()).isEqualTo(seller.getUserId());
        }
    }

    @Test
    void test_getCountOfItemsByUserId() {
        User seller2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());

        for (int i = 0; i < 20; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller, category));
        }

        for (int i = 0; i < 15; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller2, category));
        }

        int countOfItemsByUserId = itemService.getCountOfItemsByUserId(seller.getUserId());
        assertThat(countOfItemsByUserId).isEqualTo(21);
    }

    @Test
    void test_getTotalItemCount() {
        User seller2 = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());

        for (int i = 0; i < 20; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller, category));
        }

        for (int i = 0; i < 15; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller2, category));
        }

        int totalItemCount = itemService.getTotalItemCount();
        assertThat(totalItemCount).isEqualTo(20 + 15 + 1);
    }

}
