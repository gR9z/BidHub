package fr.eni.tp.auctionapp.dao;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.controller.UserProfileController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserProfileControllerTest {

    @Autowired
    private UserProfileController userProfileController;

    @Autowired
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(
                "user",
                "bonjour",
                "bonsoir",
                "test@genie.com",
                "0123456789",
                "123 rue de Test",
                "75001",
                "Paris",
                "password",
                100,
                false
        );
        userService.saveUser(testUser);
    }

    @Test
    void testDeleteUser_Success() {
        System.out.println(testUser);
        userProfileController.deleteUser("userTEST", null);

    }

}
