package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.dal.CategoryDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    CategoryDao categoryDao;

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public void createCategory(Category category) {
        categoryDao.insert(category);
    }

    @Override
    public Optional<Category> findCategoryById(int id) {
        return categoryDao.getById(id);
    }

    @Override
    public void updateCategory(Category category) {
        categoryDao.update(category);
    }

    @Override
    public void removeCategoryById(int id) {
        categoryDao.deleteById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }
}
