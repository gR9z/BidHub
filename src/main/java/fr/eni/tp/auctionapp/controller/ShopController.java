package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ShopController {

    private ItemService itemService;
    private CategoryService categoryService;

    public ShopController(ItemService itemService, CategoryService categoryService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    @GetMapping("/shop")
    public String shop(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        List<Item> items = itemService.getAllPaginated(page, size);
        List<Category> categories = categoryService.getAllCategories();
        int totalItems = itemService.getTotalItemCount();
        int totalPages = itemService.getTotalPageCount(size);

        model.addAttribute("items", items);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "shop.html";
    }

}
