package fr.eni.tp.auctionapp;

import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.User;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class DatabaseSeeder {

    static TestDatabaseService testDatabaseService;

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

        System.out.printf("%d users and 1 admin populated in the database.\n", count);
    }

    public static void categorySeeder(int count) {

        for (int i = 0; i < count - 1; i++) {
            Category category = testDatabaseService.createRandomCategory();
            testDatabaseService.insertCategoryInDatabase(category);
        }

        System.out.printf("%d categories populated in the database.\n", count);
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(fr.eni.tp.auctionapp.AuctionAppApplication.class, args);
        testDatabaseService = context.getBean(TestDatabaseService.class);
        testDatabaseService.clearDatabase();

        userSeeder(50);
        categorySeeder(20);

        context.close();
    }
}
