package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategoryDao {

    void insert(Category category);

    Optional<Category> getById(int id);

    void update(Category category);

    void deleteById(int id);

    List<Category> findAll();

    Map<Integer, Integer> countPerCategory();
}
