package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.Item;

public interface ItemService {
    void createItem(Item item);
    Item read(int id);
}
