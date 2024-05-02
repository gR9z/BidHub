package fr.eni.tp.auctionapp;

import com.github.javafaker.Faker;
import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestDatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemService itemService;

    private Faker faker = new Faker();

    public void clearDatabase() {

        String[] tables = new String[]{"auctions", "withdrawals", "items", "categories", "users"};

        for (String table : tables) {
            jdbcTemplate.execute("DELETE FROM " + table);
        }
    }

    public User createRandomUser() {
        User user = new User();
        user.setUsername(faker.name().username());
        user.setLastName(faker.name().lastName());
        user.setFirstName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        user.setPhone(faker.phoneNumber().phoneNumber());
        user.setStreet(faker.address().streetAddress());
        user.setZipCode(faker.address().zipCode());
        user.setCity(faker.address().city());
        user.setPassword("password");
        user.setCredit(faker.number().numberBetween(0, 5001));
        user.setAdmin(false);

        return user;
    }

    public User createRandomAdmin() {
        User user = createRandomUser();
        user.setAdmin(true);

        return user;
    }

    public User insertUserInDatabase(User user) {
        userService.createUser(user);
        return user;
    }

    public Category createRandomCategory() {
        Category category = new Category();
        category.setLabel(faker.commerce().department());

        return category;
    }

    public Category insertCategoryInDatabase(Category category) {
        categoryService.createCategory(category);
        return category;
    }

    public Item createRandomItem(User seller, Category category) {
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

        return item;
    }

    public Item insertItemInDatabase(Item item) {
        itemService.createItem(item);
        return item;
    }

}