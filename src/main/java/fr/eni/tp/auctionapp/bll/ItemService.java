package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.Item;

import java.util.Optional;

public interface ItemService {
    void createItem(Item item);
    Optional<Item> read(int id);
}
