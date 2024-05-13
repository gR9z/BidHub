package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategoryService {

    void createCategory(Category category);

    Optional<Category> findCategoryById(int id);

    void updateCategory(Category category);

    void removeCategoryById(int id);

    List<Category> getAllCategories();

    Map<Integer, Integer> getCountByCategory();
}
