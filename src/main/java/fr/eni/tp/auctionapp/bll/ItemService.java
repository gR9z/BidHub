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
    List<Item> getAllPaginated(int page, int size);
    List<Item> getByUserIdPaginated(int userId, int page, int size);
    int getCountOfItemsByUserId(int userId);
    int getTotalItemCount();
    int getTotalPageCount(int size);
}
