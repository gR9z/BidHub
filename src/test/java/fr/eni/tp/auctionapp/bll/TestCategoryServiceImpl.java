package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bll.impl.CategoryServiceImpl;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.dal.CategoryDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestCategoryServiceImpl {

    @Mock
    private CategoryDao categoryDaoMock;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_createCategory() {
        Category category = new Category();

        doNothing().when(categoryDaoMock).insert(category);

        categoryService.createCategory(category);

        verify(categoryDaoMock, times(1)).insert(category);
    }

    @Test
    void test_findCategoryById() {
        Category category = new Category();
        int categoryId = 123;

        when(categoryDaoMock.getById(categoryId)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.findCategoryById(categoryId);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(category);

        verify(categoryDaoMock).getById(categoryId);
    }

    @Test
    void test_updateCategory() {
        Category category = new Category();

        categoryService.updateCategory(category);

        verify(categoryDaoMock, times(1)).update(category);
    }

    @Test
    void test_removeCategoryById() {
        int categoryId = 123;

        categoryService.removeCategoryById(categoryId);

        verify(categoryDaoMock, times(1)).deleteById(categoryId);
    }

    @Test
    void test_getAllCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        categories.add(new Category());

        when(categoryDaoMock.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(categories.size());

        verify(categoryDaoMock).findAll();
    }
}
