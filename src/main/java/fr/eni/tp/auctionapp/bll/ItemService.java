package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    void insert(Item item);
    Optional<Item> read(int id);
    void update(Item item);
    void delete(int itemId);
    List<Item> findAll();
    List<Item> findAllItemsPaginated(int page, int size);
    List<Item> findAllItemsByUserIdPaginated(int userId, int page, int size);
    int countItemsByUserId(int userId);
    int count();
}
