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

import java.util.List;
import java.util.Optional;

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
    private Category category;

    @BeforeEach
    public void setup() {
        testDatabaseService.clearDatabase();
        category = testDatabaseService.insertCategoryInDatabase(testDatabaseService.createRandomCategory());
    }

    @Test
    void test_createCategory() {
        Category newCategory = new Category();
        newCategory.setLabel(faker.commerce().department());

        categoryDao.insert(newCategory);
        Optional<Category> optionalCategory = categoryDao.read(newCategory.getCategoryId());
        assertThat(optionalCategory.isPresent()).isTrue();

        Category category = optionalCategory.get();
        assertThat(category.getLabel()).isEqualTo(newCategory.getLabel());
    }

    @Test
    void test_readCategory() {
        Optional<Category> optionalCategory = categoryDao.read(category.getCategoryId());
        assertThat(optionalCategory.isPresent()).isTrue();

        Category getCategory = optionalCategory.get();
        assertThat(category.getLabel()).isEqualTo(getCategory.getLabel());
    }

    @Test
    void test_updateCategory() {
        category.setLabel("New Category Name");
        categoryDao.update(category);

        Optional<Category> optionalCategory = categoryDao.read(category.getCategoryId());
        assertThat(optionalCategory.isPresent()).isTrue();

        Category category = optionalCategory.get();
        assertThat(category.getLabel()).isEqualTo("New Category Name");
    }

    @Test
    void test_deleteCategory() {
        categoryDao.delete(category.getCategoryId());

        Optional<Category> optionalCategory = categoryDao.read(category.getCategoryId());
        assertThat(optionalCategory).isEmpty();
    }

    @Test
    void test_findAll() {

        for (int i = 0; i < 10; i++) {
            testDatabaseService.insertCategoryInDatabase(testDatabaseService.createRandomCategory());
        }

        List<Category> categories = categoryDao.findAll();
        assertThat(categories.size()).isEqualTo(11);
    }
}
