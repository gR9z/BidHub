package fr.eni.tp.auctionapp;

import com.github.javafaker.Faker;
import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TestDatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    private Faker faker = new Faker();

    public void clearDatabase() {

        String[] tables = new String[]{"auctions", "categories", "items", "users", "withdrawals"};

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

}