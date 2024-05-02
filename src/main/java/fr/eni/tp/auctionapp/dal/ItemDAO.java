package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.Item;

import java.util.List;

public interface ItemDAO {
    void insert(Item item);

    Item read(int id);

    void update(Item item);

    void delete(int itemId);

    List<Item> findAll();
}
