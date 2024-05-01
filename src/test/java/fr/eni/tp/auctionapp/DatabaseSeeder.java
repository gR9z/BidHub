package fr.eni.tp.auctionapp;

import fr.eni.tp.auctionapp.bo.User;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class DatabaseSeeder {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(fr.eni.tp.auctionapp.AuctionAppApplication.class, args);
        TestDatabaseService testDatabaseService = context.getBean(TestDatabaseService.class);

        testDatabaseService.clearDatabase();

        int userCount = args.length > 0 ? Integer.parseInt(args[0]) : 50;

        for (int i = 0; i < userCount - 1; i++) {
            User user = testDatabaseService.createRandomUser();
            if (i == 0) {
                user.setUsername("user");
            }
            testDatabaseService.insertUserInDatabase(user);
        }

        User admin = testDatabaseService.createRandomAdmin();
        admin.setUsername("admin");
        testDatabaseService.insertUserInDatabase(admin);

        System.out.printf("%d users and 1 admin populated in the database.\n", userCount);

        context.close();
    }
}
