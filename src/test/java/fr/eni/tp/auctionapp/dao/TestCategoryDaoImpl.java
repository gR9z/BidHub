package fr.eni.tp.auctionapp.dao;

import com.github.javafaker.Faker;
import fr.eni.tp.auctionapp.TestDatabaseService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.dal.AuctionDao;
import fr.eni.tp.auctionapp.dal.CategoryDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestCategoryDaoImpl {

    @Autowired
    TestDatabaseService testDatabaseService;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private AuctionDao auctionDao;

    private final Faker faker = new Faker();

    @BeforeEach
    public void setup() {
        testDatabaseService.clearDatabase();
    }

    @Test
    void createCategory() {
        Category category = new Category();
        category.setLabel(faker.commerce().department());

        categoryDao.insert(category);

        Category categoryInDb = categoryDao.read(category.getCategoryId());
        assertThat(categoryInDb.getLabel()).isEqualTo(category.getLabel());
    }

}
