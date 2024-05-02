package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.dal.CategoryDao;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Category read(int id) {
        return categoryDao.read(id);
    }

    @Override
    public void updateCategory(Category category) {
        categoryDao.update(category);
    }

    @Override
    public void deleteCategory(int id) {
        categoryDao.delete(id);
    }

    @Override
    public List<Category> readAll() {
        return categoryDao.findAll();
    }
}
