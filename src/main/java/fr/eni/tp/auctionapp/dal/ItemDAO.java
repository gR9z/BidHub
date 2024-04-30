package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.Item;

import java.util.List;

public interface ItemDAO {
    void insert(Item item);

    Item read(long id);

    void update(Item item);

    void delete(Item item);

    List<Item> findAll();
}
