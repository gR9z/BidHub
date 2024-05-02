package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.Category;

import java.util.List;

public interface CategoryService {

    void createCategory(Category category);

    Category read(int id);

    void updateCategory(Category category);

    void deleteCategory(int id);

    List<Category> readAll();
}
