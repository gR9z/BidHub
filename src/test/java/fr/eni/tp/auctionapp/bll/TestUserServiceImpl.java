package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestUserServiceImpl {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    void createUser() {
        User user = new User(
                "Gaétan",
                "Dupont",
                "Jean",
                "lebest@genie.com",
                "0123456789",
                "123 rue de Test",
                "75001",
                "Paris",
                "password",
                100,
                false
        );

        User admin = new User(
                "admin",
                "admin",
                "admin",
                "admin@admin.com",
                "0666666666",
                "6 rue de l'admin",
                "29000",
                "Quimper",
                "admin",
                10000,
                true
        );

//        userService.createUser(user);
//        userService.createUser(admin);

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        UserDetails adminDetails = userService.loadUserByUsername(admin.getUsername());

        assertThat(userDetails.getUsername()).isEqualTo(user.getUsername());
        assertThat(adminDetails.getUsername()).isEqualTo(admin.getUsername());
    }
}
