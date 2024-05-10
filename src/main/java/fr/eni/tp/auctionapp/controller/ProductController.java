package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.AuctionService;
import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bll.WithdrawalService;
import fr.eni.tp.auctionapp.bo.*;
import fr.eni.tp.auctionapp.dto.BidHistoryDto;
import fr.eni.tp.auctionapp.utils.URLUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    private ItemService itemService;
    private CategoryService categoryService;
    private WithdrawalService withdrawalService;
    private AuctionService auctionService;

    ProductController(ItemService itemService, CategoryService categoryService, WithdrawalService withdrawalService, AuctionService auctionService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
        this.withdrawalService = withdrawalService;
        this.auctionService = auctionService;
    }

    @GetMapping("/products/{categorySlug}/{itemName}")
    public String singleProductPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam("id") int itemId,
            @AuthenticationPrincipal User authenticatedUser,
            Model model
    ) {
        Optional<Item> optionalItem = itemService.findItemById(itemId);
        
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();

            Optional<Category> optionalCategory = categoryService.findCategoryById(item.getCategory().getCategoryId());
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                item.setCategory(category);
            }

            Optional<Withdrawal> optionalWithdrawal = withdrawalService.findWithdrawalByItemId(item.getItemId());
            if (optionalWithdrawal.isPresent()) {
                Withdrawal withdrawal = optionalWithdrawal.get();
                item.setWithdrawal(withdrawal);
            }

            List<Category> categoryList = categoryService.getAllCategories();

            List<Integer> categorySingleton = Collections.singletonList(item.getCategory().getCategoryId());
            List<Item> items = itemService.searchItems(null, categorySingleton, 1, 3);

            Map<Integer, String> itemUrls = items.stream()
                    .collect(Collectors.toMap(Item::getItemId, getItem -> URLUtils.toFriendlyURL(getItem.getItemName()) + "?id=" + getItem.getItemId()));

            Map<Integer, String> categoryUrl = categoryList.stream()
                    .collect(Collectors.toMap(
                            Category::getCategoryId,
                            category -> (category.getLabel() != null) ? URLUtils.toFriendlyURL(category.getLabel()) : "",
                            (existingValue, newValue) -> existingValue
                    ));

            List<BidHistoryDto> bidHistory = auctionService.getItemBidHistoryPaginated(item.getItemId(), page, size);

            Auction auction = new Auction();
            if(authenticatedUser != null) {
                auction.setItemId(item.getItemId());
                auction.setUserId(authenticatedUser.getUserId());

                int bidPlusTenPerCent = (int) Math.ceil(item.getSellingPrice() * 1.10);
                auction.setBidAmount(bidPlusTenPerCent);
            }

            model.addAttribute("itemUrls", itemUrls);
            model.addAttribute("categoryUrl", categoryUrl);
            model.addAttribute("items", items);
            model.addAttribute("bids", bidHistory);
            model.addAttribute("item", item);
            model.addAttribute("auction", auction);
        } else {
            return null;
        }

        return "product.html";
    }
}
