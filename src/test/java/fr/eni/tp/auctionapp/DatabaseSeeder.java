package fr.eni.tp.auctionapp;

import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Random;

public class DatabaseSeeder {

    static TestDatabaseService testDatabaseService;
    static UserService userService;
    static CategoryService categoryService;
    static ItemService itemService;

    public static void userSeeder(int count) {

        for (int i = 0; i < count - 1; i++) {
            User user = testDatabaseService.createRandomUser();
            if (i == 0) {
                user.setUsername("user");
            }
            testDatabaseService.insertUserInDatabase(user);
        }

        User admin = testDatabaseService.createRandomAdmin();
        admin.setUsername("admin");
        testDatabaseService.insertUserInDatabase(admin);

        System.out.printf("%d users and 1 admin populated in the database.%n", count);
    }

    public static void categorySeeder(int count) {

        for (int i = 0; i < count - 1; i++) {
            Category category = testDatabaseService.createRandomCategory();
            testDatabaseService.insertCategoryInDatabase(category);
        }

        System.out.printf("%d categories populated in the database.%n", count);
    }

    public static void itemSeeder(int count) {
        List<User> users = userService.getAllUsers();
        List<Category> categories = categoryService.getAllCategories();

        Random random = new Random();
        for (int i = 0; i < count - 1; i++) {
            User user = users.get(random.nextInt(users.size()));
            Category category = categories.get(random.nextInt(categories.size()));

            Item item = testDatabaseService.createRandomItem(user, category);
            testDatabaseService.insertItemInDatabase(item);
        }

        System.out.printf("%d items populated in the database.%n", count);
    }

    public static void auctionSeeder(int count) {
        List<User> users = userService.getAllUsers();
        List<Item> items = itemService.getAllItems();

        Random random = new Random();
        for (int i = 0; i < count - 1; i++) {
            User user = users.get(random.nextInt(users.size()));
            Item item = items.get(random.nextInt(items.size()));

            Auction auction = testDatabaseService.createAuction(user, item);
            testDatabaseService.insertAuctionInDatabase(auction);
        }

        System.out.printf("%d auctions populated in the database.%n", count);
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(fr.eni.tp.auctionapp.AuctionAppApplication.class, args);
        testDatabaseService = context.getBean(TestDatabaseService.class);
        userService = context.getBean(UserService.class);
        categoryService = context.getBean(CategoryService.class);
        itemService = context.getBean(ItemService.class);

        testDatabaseService.clearDatabase();

        userSeeder(100);
        categorySeeder(20);
        itemSeeder(300);
        auctionSeeder(2000);

        context.close();
    }
}
