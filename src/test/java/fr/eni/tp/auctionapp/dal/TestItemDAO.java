package fr.eni.tp.auctionapp.dal;


import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestItemDAO {

    @Autowired
    private ItemDAO itemDAO;


    @Test
    void read() {
        int testId = 5;
        Item item = itemDAO.read(testId);
        assertThat(item.getItemName()).isEqualTo("Sofa");
    }

    @Test
    void findAll(){
        List<Item> items = itemDAO.findAll();
        assertThat(items.size()).isEqualTo(2);
    }

    @Test
    void insert(){
        User user = new User(
                1,
                "admin",
                "admin",
                "admin",
                "admin@admin.com",
                "0666666666",
                "6 rue de l'admin",
                "29000",
                "Quimper",
                "admin",
                10000,
                true
        );
        Category category = new Category(1, "Category 1");
        Item item = new Item("itemName", "description", category, 200, LocalDateTime.now(), LocalDateTime.of(2024, 05, 01, 10, 30), user);
        itemDAO.insert(item);
    }

}
