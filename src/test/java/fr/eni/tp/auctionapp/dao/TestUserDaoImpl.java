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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

@SpringBootTest
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

    @Test
    void test_updateUser() {
        user.setEmail("another@email.com");
        userDao.updateUser(user);

        Optional<User> optionalUser = userDao.selectUserByUsername(user.getUsername());
        assertThat(optionalUser.isPresent()).isTrue();
        assertThat(optionalUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void test_deleteUser() {
        userDao.deleteUser(user.getId());
        Optional<User> optionalUser = userDao.selectUserByUsername(user.getUsername());
        assertThat(optionalUser.isPresent()).isFalse();
    }

    @Test
    void test_countUsers() {
        for (int i = 0; i < 25; i++) {
            testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        }

        assertThat(userDao.count()).isEqualTo(25 + 1);
    }

    @Test
    void test_findAll() {

        for (int i = 0; i < 10; i++) {
            testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        }

        List<User> users = userDao.findAll();
        assertThat(users.size()).isEqualTo(11);
    }

    @Test
    void test_findAllUsersPagination() {
        for (int i = 0; i < 20; i++) {
            testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        }

        List<User> users = userDao.findAllUsersPagination(1, 10);
        assertThat(users.size()).isEqualTo(10);
    }
}