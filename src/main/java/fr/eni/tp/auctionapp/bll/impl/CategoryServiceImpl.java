package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.dal.CategoryDao;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;
    private final Map<Integer, Category> categoryCache = new LinkedHashMap<>();

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @PostConstruct
    public void preloadCache() {
        List<Category> categories = categoryDao.findAll();

        for (Category category : categories) {
            categoryCache.put(category.getCategoryId(), category);
        }
    }

    @Override
    public void createCategory(Category category) {
        categoryDao.insert(category);
        categoryCache.put(category.getCategoryId(), category);
    }

    @Override
    public Optional<Category> findCategoryById(int id) {
        return Optional.ofNullable(categoryCache.getOrDefault(id, categoryDao.getById(id).orElse(null)));
    }

    @Override
    public void updateCategory(Category category) {
        categoryDao.update(category);
        categoryCache.put(category.getCategoryId(), category);
    }

    @Override
    public void removeCategoryById(int id) {
        categoryDao.deleteById(id);
        categoryCache.remove(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return new ArrayList<>(categoryCache.values());
    }

    @Override
    public Map<Integer, Integer> getCountByCategory() {
        return categoryDao.countPerCategory();
    }
}
