package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;

import java.util.List;

public interface CategoryDao {

    void insert(Category category);

    Category read(int id);

    void update(Category category);

    void delete(int id);

    List<Category> findAll();

}
