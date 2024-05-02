package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.dal.ItemDao;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemDao itemDAO;

    public ItemServiceImpl(ItemDao itemDAO) {
        this.itemDAO = itemDAO;
    }

    @Override
    public void createItem(Item item) {
        //Faire tout les isValid

        itemDAO.insert(item);
    }

    @Override
    public Optional<Item> read(int id) {
        return itemDAO.read(id);
    }
}
