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
    void test_createItem() {
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

        Optional<Item> optionalItem = itemDao.read(item.getItemId());
        assertThat(optionalItem.isPresent()).isTrue();
        Item itemRead = optionalItem.get();

        assertThat(itemRead.getItemId()).isEqualTo(item.getItemId());
        assertThat(itemRead.getItemName()).isEqualTo(item.getItemName());
        assertThat(itemRead.getDescription()).isEqualTo(item.getDescription());
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


}
