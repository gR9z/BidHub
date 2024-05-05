package fr.eni.tp.auctionapp.bll;

import com.github.javafaker.Faker;
import fr.eni.tp.auctionapp.TestDatabaseService;
import fr.eni.tp.auctionapp.bo.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TestCategoryServiceImpl {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TestDatabaseService testDatabaseService;

    private final Faker faker = new Faker();

    @BeforeEach
    public void setUp() {
        testDatabaseService.clearDatabase();
    }

    @Test
    public void test_createCategory() {
        Category category = testDatabaseService.createRandomCategory();
        categoryService.createCategory(category);

        Optional<Category> optionalCategory = categoryService.findCategoryById(category.getCategoryId());
        assertThat(optionalCategory.isPresent()).isTrue();

        Category getCategory = optionalCategory.orElseThrow(() -> new IllegalStateException("Category not found"));
        assertThat(category.getLabel()).isEqualTo(getCategory.getLabel());
    }

    @Test
    public void test_findCategoryById() {
        Category category = testDatabaseService.createRandomCategory();
        categoryService.createCategory(category);

        Optional<Category> optionalCategory = categoryService.findCategoryById(category.getCategoryId());
        Category getCategory = optionalCategory.orElseThrow(() -> new IllegalStateException("Category not found"));

        assertThat(category.getLabel()).isEqualTo(getCategory.getLabel());
    }

    @Test
    public void test_updateCategory() {
        Category category = testDatabaseService.createRandomCategory();
        categoryService.createCategory(category);

        category.setLabel("New Label Category");
        categoryService.updateCategory(category);

        Optional<Category> optionalCategory = categoryService.findCategoryById(category.getCategoryId());
        Category getCategory = optionalCategory.orElseThrow(() -> new IllegalStateException("Category not found"));
        assertThat(category.getLabel()).isEqualTo(getCategory.getLabel()).isEqualTo("New Label Category");
    }

    @Test
    public void test_removeCategoryById() {
        Category category = testDatabaseService.createRandomCategory();
        categoryService.createCategory(category);
        categoryService.removeCategoryById(category.getCategoryId());
        Optional<Category> optionalCategory = categoryService.findCategoryById(category.getCategoryId());

        assertThat(optionalCategory.isPresent()).isFalse();
    }

    @Test
    public void test_getAllCategories() {
        for (int i = 0; i < 10; i++) {
            Category category = testDatabaseService.createRandomCategory();
            testDatabaseService.insertCategoryInDatabase(category);
        }

        List<Category> categories = categoryService.getAllCategories();
        assertThat(categories.size()).isEqualTo(10);
    }
}
