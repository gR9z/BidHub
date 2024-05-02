package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    void insert(Item item);

    Optional<Item> read(int id);

    void update(Item item);

    void delete(Item item);

    List<Item> findAll();
}
