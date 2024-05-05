package fr.eni.tp.auctionapp.bll;

import com.github.javafaker.Faker;
import fr.eni.tp.auctionapp.TestDatabaseService;
import fr.eni.tp.auctionapp.bo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TestUserServiceImpl {

    @Autowired
    private UserService userService;

    @Autowired
    private TestDatabaseService testDatabaseService;

    User adminUser;
    private final Faker faker = new Faker();

    @BeforeEach
    public void setUp() {
        testDatabaseService.clearDatabase();
        adminUser = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomAdmin());
    }

    @Test
    void test_createUser() {
        User adminUser = new User();
        adminUser.setUsername(faker.name().username());
        adminUser.setLastName(faker.name().lastName());
        adminUser.setFirstName(faker.name().firstName());
        adminUser.setEmail(faker.internet().emailAddress());
        adminUser.setPhone(faker.phoneNumber().phoneNumber());
        adminUser.setStreet(faker.address().streetAddress());
        adminUser.setZipCode(faker.address().zipCode());
        adminUser.setCity(faker.address().city());
        adminUser.setPassword(faker.internet().password());
        adminUser.setCredit(10000);
        adminUser.setAdmin(true);

        userService.createUser(adminUser);
        UserDetails loadedAdminUser = userService.loadUserByUsername(adminUser.getUsername());
        User castLoadedAdminUser = (User) loadedAdminUser;

        assertThat(castLoadedAdminUser.getUsername()).isEqualTo(adminUser.getUsername());
        assertThat(castLoadedAdminUser.isAdmin()).isTrue();
    }

    @Test
    void test_loadUserByUsername() {
        UserDetails loadedUser = userService.loadUserByUsername(adminUser.getUsername());
        User castLoadedUser = (User) loadedUser;

        assertThat(castLoadedUser.getUsername()).isEqualTo(adminUser.getUsername());
        assertThat(castLoadedUser.isAdmin()).isTrue();
    }

    @Test
    void test_getAllUsers() {
        for (int i = 0; i < 12; i++) {
            testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        }

        List<User> users = userService.getAllUsers();
        assertThat(users.size()).isEqualTo(12 + 1);
    }

    @Test
    void test_getTotalUserCount() {
        for (int i = 0; i < 14; i++) {
            testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        }

        int count = userService.getTotalUserCount();
        assertThat(count).isEqualTo(14 + 1);
    }
}
