package fr.eni.tp.auctionapp;

import fr.eni.tp.auctionapp.bll.AuctionService;
import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class DatabaseSeeder {

    static TestDatabaseService testDatabaseService;
    static UserService userService;
    static CategoryService categoryService;
    static ItemService itemService;
    static AuctionService auctionService;

    public static void userSeeder(int count) {

        for (int i = 0; i < count; i++) {
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

        for (int i = 0; i < count; i++) {
            Category category = testDatabaseService.createRandomCategory();
            testDatabaseService.insertCategoryInDatabase(category);
        }

        System.out.printf("%d categories populated in the database.%n", count);
    }

    public static void itemSeeder(int count) {
        List<User> users = userService.getAllUsers();
        List<Category> categories = categoryService.getAllCategories();

        Random random = new Random();
        for (int i = 0; i < count; i++) {
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
        for (int i = 0; i < count; i++) {
            User user = users.get(random.nextInt(users.size()));
            Item item = items.get(random.nextInt(items.size()));

            List<Auction> auctions = auctionService.getAllAuctionsByItemId(item.getItemId());
            auctions.sort(Comparator.comparing(Auction::getAuctionDate));

            Auction lastAuction = (auctions.isEmpty() ? null : auctions.get(auctions.size() - 1));

            int currentHighestBid = (lastAuction != null ? lastAuction.getBidAmount() : item.getStartingPrice());

            Auction auction = testDatabaseService.createAuction(user, item, currentHighestBid);
            testDatabaseService.insertAuctionInDatabase(auction);
            item.setSellingPrice(auction.getBidAmount());
            itemService.updateItem(item);
        }

        System.out.printf("%d auctions populated in the database.%n", count);
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(fr.eni.tp.auctionapp.AuctionAppApplication.class, args);
        testDatabaseService = context.getBean(TestDatabaseService.class);
        userService = context.getBean(UserService.class);
        categoryService = context.getBean(CategoryService.class);
        itemService = context.getBean(ItemService.class);
        auctionService = context.getBean(AuctionService.class);

        testDatabaseService.clearDatabase();

        userSeeder(20);
        categorySeeder(20);
        itemSeeder(100);
        auctionSeeder(800);

        context.close();
    }
}
