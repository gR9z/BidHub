package fr.eni.tp.auctionapp.dao;

import com.github.javafaker.Faker;
import fr.eni.tp.auctionapp.TestDatabaseService;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TestUserDaoImpl {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TestDatabaseService testDatabaseService;

    User user;
    private final Faker faker = new Faker();

    @BeforeEach
    public void setup() {
        testDatabaseService.clearDatabase();
        user = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
    }

    @Test
    void test_selectUserByUsername() {
        Optional<User> optionalUser = userDao.selectUserByUsername(user.getUsername());
        assertThat(optionalUser).isPresent();
        assertThat(optionalUser.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void test_insertUser() {
        User newUser = new User();
        newUser.setUsername(faker.name().username());
        newUser.setLastName(faker.name().lastName());
        newUser.setFirstName(faker.name().firstName());
        newUser.setEmail(faker.internet().emailAddress());
        newUser.setPhone(faker.phoneNumber().phoneNumber());
        newUser.setStreet(faker.address().streetAddress());
        newUser.setZipCode(faker.address().zipCode());
        newUser.setCity(faker.address().city());
        newUser.setPassword(faker.internet().password());
        newUser.setCredit(10000);
        newUser.setAdmin(true);
        userDao.insertUser(newUser);

        Optional<User> optionalNewUser = userDao.selectUserByUsername(newUser.getUsername());
        assertThat(optionalNewUser.isPresent()).isTrue();
        assertThat(optionalNewUser.get().getFirstName()).isEqualTo(newUser.getFirstName());
    }
}