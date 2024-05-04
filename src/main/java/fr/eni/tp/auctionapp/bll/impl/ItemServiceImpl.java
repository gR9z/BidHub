package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.dal.ItemDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemDao itemDAO;

    public ItemServiceImpl(ItemDao itemDAO) {
        this.itemDAO = itemDAO;
    }

    @Override
    public void insert(Item item) {
        itemDAO.insert(item);
    }

    @Override
    public Optional<Item> read(int id) {
        return itemDAO.read(id);
    }

    @Override
    public void update(Item item) {
        itemDAO.update(item);
    }

    @Override
    public void delete(int itemId) {
        itemDAO.delete(itemId);
    }

    @Override
    public List<Item> findAll() {
        return itemDAO.findAll();
    }

    @Override
    public List<Item> findAllItemsPaginated(int page, int size) {
        return itemDAO.findAllItemsPaginated(page, size);
    }

    @Override
    public List<Item> findAllItemsByUserIdPaginated(int userId, int page, int size) {
        return itemDAO.findAllItemsByUserIdPaginated(userId, page, size);
    };

    @Override
    public int countItemsByUserId(int userId) {
        return itemDAO.countItemsByUserId(userId);
    }

    @Override
    public int count() {
        return itemDAO.count();
    }
}
