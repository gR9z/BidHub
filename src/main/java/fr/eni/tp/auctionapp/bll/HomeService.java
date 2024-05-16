package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.Item;

import java.util.List;

public interface HomeService {
    public List<Item> findLastAddedItems();
}
