package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.HomeService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.utils.URLUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private HomeService homeService;
    private CategoryService categoryService;

    public HomeController(HomeService homeService, CategoryService categoryService) {
        this.homeService = homeService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String home(
            Model model
    ) {
        List<Item> items = homeService.findLastAddedItems();
        List<Category> categoryList = categoryService.getAllCategories();

        Map<Integer, String> itemUrls = items.stream()
                .collect(Collectors.toMap(Item::getItemId, item -> URLUtils.toFriendlyURL(item.getItemName()) + "?id=" + item.getItemId()));

        Map<Integer, String> categoryUrl = categoryList.stream()
                .collect(Collectors.toMap(
                        Category::getCategoryId,
                        category -> (category.getLabel() != null) ? URLUtils.toFriendlyURL(category.getLabel()) : "",
                        (existingValue, newValue) -> existingValue
                ));

        model.addAttribute("items", items);
        model.addAttribute("itemUrls", itemUrls);
        model.addAttribute("categoryUrl", categoryUrl);

        System.out.println(items);
        return "home.html";
    }




}
