package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestUserServiceImpl {

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.update("DELETE FROM users");
    }

    @Test
    void contextLoads() {
    }

    @Test
    void createUser() {
        User user = new User(
                "user",
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

        userService.createUser(user);
        UserDetails loadedUser = userService.loadUserByUsername("user");
        User castLoadedUser = (User) loadedUser;

        userService.createUser(admin);
        UserDetails loadedAdminUser = userService.loadUserByUsername("admin");
        User castLoadedAdminUser = (User) loadedAdminUser;

        assertThat(castLoadedUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(castLoadedUser.isAdmin()).isFalse();
        assertThat(castLoadedAdminUser.getUsername()).isEqualTo(admin.getUsername());
        assertThat(castLoadedAdminUser.isAdmin()).isTrue();
    }
}
