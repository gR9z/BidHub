package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.HomeService;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.dal.ItemDao;
import org.springframework.stereotype.Service;

import java.util.List;

import static fr.eni.tp.auctionapp.bll.impl.ItemServiceImpl.calculateItemStatus;

@Service
public class HomeServiceImpl implements HomeService {

    private ItemDao itemDao;

    public HomeServiceImpl(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public List<Item> findLastAddedItems() {
        List<Item> items = itemDao.findLastAddedItems();
        items.forEach(item -> item.setSaleStatus(calculateItemStatus(item)));
        return items;
    }
}
