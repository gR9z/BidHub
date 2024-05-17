package fr.eni.tp.auctionapp.dao;

import com.github.javafaker.Faker;
import fr.eni.tp.auctionapp.TestDatabaseService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.bo.Withdrawal;
import fr.eni.tp.auctionapp.dal.ItemDao;
import fr.eni.tp.auctionapp.dal.WithdrawalDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestWithdrawalDaoImpl {

    @Autowired
    private WithdrawalDao withdrawalDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private TestDatabaseService testDatabaseService;

    private final Faker faker = new Faker();

    private User user;
    private Category category;

    @BeforeEach
    public void setup() {
        testDatabaseService.clearDatabase();
        user = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
        category = testDatabaseService.insertCategoryInDatabase(testDatabaseService.createRandomCategory());
    }

    @Test
    void test_insert() {
        Item item = testDatabaseService.createRandomItem(user, category);
        Withdrawal withdrawal = testDatabaseService.createRandomWithdrawal(item);

        itemDao.insert(item);
        withdrawalDao.insert(withdrawal);

        Optional<Withdrawal> optionalWithdrawal = withdrawalDao.getByItemId(item.getItemId());
        assertThat(optionalWithdrawal.isPresent()).isTrue();
        Withdrawal getWithdrawal = optionalWithdrawal.get();
        assertThat(getWithdrawal.getItemId()).isEqualTo(item.getItemId());
    }

    @Test
    void test_getById() {
        Item item = testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(user, category));
        Optional<Withdrawal> optionalWithdrawal = withdrawalDao.getByItemId(item.getItemId());
        assertThat(optionalWithdrawal.isPresent()).isTrue();
        Withdrawal getWithdrawal = optionalWithdrawal.get();
        assertThat(getWithdrawal.getItemId()).isEqualTo(item.getItemId());
    }

    @Test
    void test_update() {
        Item item = testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(user, category));
        Withdrawal withdrawal = testDatabaseService.createRandomWithdrawal(item);

        withdrawal.setCity("New City");
        withdrawal.setStreet("New Street");
        withdrawal.setZipCode("New Zip Code");
        withdrawalDao.update(withdrawal);

        Optional<Withdrawal> optionalWithdrawal = withdrawalDao.getByItemId(item.getItemId());
        assertThat(optionalWithdrawal.isPresent()).isTrue();
        Withdrawal updatedWithdrawal = optionalWithdrawal.get();

        assertThat(updatedWithdrawal.getCity()).isEqualTo("New City");
        assertThat(updatedWithdrawal.getStreet()).isEqualTo("New Street");
        assertThat(updatedWithdrawal.getZipCode()).isEqualTo("New Zip Code");
    }

    @Test
    void test_delete() {
        Item item = testDatabaseService.insertItemInDatabase(testDatabaseService.createRandomItem(user, category));

        withdrawalDao.deleteByItemId(item.getItemId());

        Optional<Withdrawal> optionalWithdrawal = withdrawalDao.getByItemId(item.getItemId());
        assertThat(optionalWithdrawal.isPresent()).isFalse();
    }
}
