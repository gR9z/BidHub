package fr.eni.tp.auctionapp.dao;

import com.github.javafaker.Faker;
import fr.eni.tp.auctionapp.TestDatabaseService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.ItemDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestItemDaoImpl {

    @Autowired
    ItemDao itemDao;

    @Autowired
    TestDatabaseService testDatabaseService;

    private final Faker faker = new Faker();

    @BeforeEach
    public void setup() {
        testDatabaseService.clearDatabase();
    }

    @Test
    void test_insert() {
        User seller = testDatabaseService.createRandomUser();
        testDatabaseService.insertUserInDatabase(seller);

        Category category = testDatabaseService.createRandomCategory();
        testDatabaseService.insertCategoryInDatabase(category);

        Item item = new Item();

        item.setItemName(faker.commerce().productName());
        item.setDescription(faker.lorem().sentence());
        item.setAuctionStartingDate(LocalDateTime.now());
        item.setAuctionEndingDate(LocalDateTime.now().plusDays(7));

        int startingPrice = faker.number().numberBetween(100, 1000);
        item.setStartingPrice(startingPrice);
        item.setSellingPrice(startingPrice + faker.number().numberBetween(1, 500));
        item.setImageUrl(faker.internet().url());

        item.setSeller(seller);
        item.setCategory(category);

        itemDao.insert(item);

        Optional<Item> optionalItem = itemDao.findAllPagination(item.getItemId());
        assertThat(optionalItem.isPresent()).isTrue();
        Item itemRead = optionalItem.get();

        assertThat(itemRead.getItemId()).isEqualTo(item.getItemId());
        assertThat(itemRead.getItemName()).isEqualTo(item.getItemName());
        assertThat(itemRead.getDescription()).isEqualTo(item.getDescription());
    }

    @Test
    void test_update() {
        User seller = testDatabaseService.createRandomUser();
        testDatabaseService.insertUserInDatabase(seller);

        Category category = testDatabaseService.createRandomCategory();
        testDatabaseService.insertCategoryInDatabase(category);

        Item item = testDatabaseService.createRandomItem(seller, category);
        testDatabaseService.insertItemInDatabase(item);

        item.setItemName("New Item Name");
        itemDao.update(item);

        Optional<Item> optionalItem = itemDao.findAllPagination(item.getItemId());
        assertThat(optionalItem.isPresent()).isTrue();
        Item itemRead = optionalItem.get();
        assertThat(itemRead.getItemId()).isEqualTo(item.getItemId());
        assertThat(itemRead.getItemName()).isEqualTo(item.getItemName());
    }

    @Test
    void test_deleteById() {
        User seller = testDatabaseService.createRandomUser();
        testDatabaseService.insertUserInDatabase(seller);

        Category category = testDatabaseService.createRandomCategory();
        testDatabaseService.insertCategoryInDatabase(category);

        Item item = testDatabaseService.createRandomItem(seller, category);
        testDatabaseService.insertItemInDatabase(item);

        itemDao.deleteById(item.getItemId());
        Optional<Item> optionalItem = itemDao.findAllPagination(item.getItemId());
        assertThat(optionalItem.isPresent()).isFalse();
    }

    @Test
    void test_findAll() {
        User seller = testDatabaseService.createRandomUser();
        testDatabaseService.insertUserInDatabase(seller);

        Category category = testDatabaseService.createRandomCategory();
        testDatabaseService.insertCategoryInDatabase(category);

        for (int i = 0; i < 10; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller, category));
        }

        List<Item> listItems = itemDao.findAll();
        System.out.println(listItems);
        assertThat(listItems.size()).isEqualTo(10);
    }

    @Test
    void test_findAllPaginated() {
        User seller = testDatabaseService.createRandomUser();
        testDatabaseService.insertUserInDatabase(seller);

        Category category = testDatabaseService.createRandomCategory();
        testDatabaseService.insertCategoryInDatabase(category);

        for (int i = 0; i < 50; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller, category));
        }

        List<Item> paginatedItems = itemDao.findAllPaginated(2, 10);
        System.out.println(paginatedItems);
        assertThat(paginatedItems.size()).isEqualTo(10);
    }

    @Test
    void findAllByUserIdPaginated() {
        User seller = testDatabaseService.createRandomUser();
        testDatabaseService.insertUserInDatabase(seller);

        User seller2 = testDatabaseService.createRandomUser();
        testDatabaseService.insertUserInDatabase(seller2);

        Category category = testDatabaseService.createRandomCategory();
        testDatabaseService.insertCategoryInDatabase(category);

        for (int i = 0; i < 15; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller, category));
        }

        for (int i = 0; i < 10; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller2, category));
        }

        List<Item> itemsFromSellerPagination = itemDao.findAllByUserIdPaginated(seller.getUserId(), 1, 100);
        List<Item> itemsFromSeller2Pagination = itemDao.findAllByUserIdPaginated(seller2.getUserId(), 1, 5);

        assertThat(itemsFromSellerPagination.size()).isEqualTo(15);
        assertThat(itemsFromSeller2Pagination.size()).isEqualTo(5);

        LocalDateTime previousDate = LocalDateTime.MIN;
        for (Item item : itemsFromSellerPagination) {
            assertTrue(item.getAuctionStartingDate().isEqual(previousDate) || item.getAuctionStartingDate().isAfter(previousDate));
            previousDate = item.getAuctionStartingDate();
        }
    }

    @Test
    void test_countByUserId() {
        User seller = testDatabaseService.createRandomUser();
        testDatabaseService.insertUserInDatabase(seller);

        User seller2 = testDatabaseService.createRandomUser();
        testDatabaseService.insertUserInDatabase(seller2);

        Category category = testDatabaseService.createRandomCategory();
        testDatabaseService.insertCategoryInDatabase(category);

        for (int i = 0; i < 15; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller, category));
        }

        for (int i = 0; i < 10; i++) {
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller2, category));
        }

        int countItemsSeller1 = itemDao.countByUserId(seller.getUserId());
        int countItemsSeller2 = itemDao.countByUserId(seller2.getUserId());

        assertThat(countItemsSeller1).isEqualTo(15);
        assertThat(countItemsSeller2).isEqualTo(10);
    }

    @Test
    void test_count() {
        User seller = testDatabaseService.createRandomUser();
        testDatabaseService.insertUserInDatabase(seller);

        Category category = testDatabaseService.createRandomCategory();
        testDatabaseService.insertCategoryInDatabase(category);

        for (int i = 0; i < 38; i++) {
            testDatabaseService.createRandomItem(seller, category);
            testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(seller, category));
        }

        int count = itemDao.count();
        assertThat(count).isEqualTo(38);
    }
}
