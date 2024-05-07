package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/shop", method = RequestMethod.GET)
    public String shop(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) List<Integer> categories,
            Model model
    ) {
        List<Item> items;
        int totalItems;
        int totalPages;

        if (q != null || categories != null) {
            items = itemService.searchItems(q, categories, (page - 1) * size, size);
            totalItems = itemService.countFilteredItems(q, categories);
        } else {
            items = itemService.getAllPaginated((page - 1) * size, size);
            totalItems = itemService.getTotalItemCount();
        }

        totalPages = (int) Math.ceil((double) totalItems / size);

        List<Category> allCategories = categoryService.getAllCategories();

        model.addAttribute("items", items);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("categories", allCategories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        model.addAttribute("q", q);
        model.addAttribute("categoriesSelected", categories);

        return "shop.html";
    }


}
