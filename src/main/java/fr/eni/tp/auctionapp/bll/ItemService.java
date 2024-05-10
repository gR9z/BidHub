package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.Item;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    void createItem(Item item);
    Optional<Item> findItemById(int id);
    void updateItem(Item item);
    void removeItemById(int itemId);
    List<Item> getAllItems();
    List<Item> searchItems(String query, List<Integer> categories, int offset, int limit);
    List<Item> getAllPaginated(int page, int size);
    List<Item> getByUserIdPaginated(int userId, int page, int size);
    int getCountOfItemsByUserId(int userId);
    int getTotalItemCount();
    int getTotalItemCountByCategory(int categoryId);
    int countFilteredItems(String query, List<Integer> categories);
    int getTotalPageCount(int size);
}
