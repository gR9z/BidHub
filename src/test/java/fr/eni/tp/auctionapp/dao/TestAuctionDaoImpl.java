package fr.eni.tp.auctionapp.dao;

import fr.eni.tp.auctionapp.TestDatabaseService;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.AuctionDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestAuctionDaoImpl {

    @Autowired
    private AuctionDao auctionDao;

    @Autowired
    private TestDatabaseService testDatabaseService;

    User user;

    @BeforeEach
    public void setup() {
        testDatabaseService.clearDatabase();
        user = testDatabaseService.insertUserInDatabase(testDatabaseService.createRandomUser());
    }

    @Test
    void test_insertAuction() {
//        auctionDao.insert();
    }
}
