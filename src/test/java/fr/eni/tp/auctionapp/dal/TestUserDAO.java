package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.Optional;

@SpringBootTest
public class TestUserDAO {

    @Autowired
    private UserDao userDao;

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
    void editUserProfile() {
        User user = new User(
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

        userDao.insertUser(user);
        user.setCity("quimper");
        userDao.editUserProfile(user);
        Optional<User> getUser = userDao.selectUserByUsername("user");
        User newUser = getUser.get();
        assertThat(newUser.getCity()).isEqualTo("quimper");

    }
}
