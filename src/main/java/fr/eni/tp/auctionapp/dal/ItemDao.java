package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ItemDao {
    void insert(Item item);

    Optional<Item> findById(int id);

    void update(Item item);

    void deleteById(int itemId);

    List<Item> findAll();

    List<Item> findAllPaginated(int page, int size);

    List<Item> searchItems(String query, List<Integer> categories, int offset, int limit);

    List<Item> findAllByUserIdPaginated(int userId, int page, int size);

    List<Item> findByCategoryPaginated(int categoryId, int page, int size);

    int countFilteredItems(String query, List<Integer> categories);

    int countByUserId(int userId);
    int countByCategoryId(int categoryId);

    int count();
}
