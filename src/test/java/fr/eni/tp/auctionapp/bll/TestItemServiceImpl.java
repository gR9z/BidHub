package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestItemServiceImpl {

    @Autowired
    private ItemService itemService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @Test
//    void createItem() {
//        Category category = new Category(1, "Category 1");
//        Item item = new Item(
//                "Machin", "Trop cool. Achète ma merde.", category, 200, LocalDateTime.now(), LocalDateTime.of(2024, 05, 01, 10, 30)
//        );
//        System.out.println(item);
//    }


}
