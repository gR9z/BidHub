package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.bo.Item;

public interface AuctionDao {

    void insert(Item item, int auctionPrice);
    Auction read(int id);

}
