package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.AuctionService;
import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bll.WithdrawalService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.Withdrawal;
import fr.eni.tp.auctionapp.dto.BidHistoryDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/products/{itemName}")
    public String singleProductPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @PathVariable String itemName,
            @RequestParam("id") int itemId,
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

            List<BidHistoryDto> bidHistory = auctionService.getItemBidHistoryPaginated(item.getItemId(), page, size);

            model.addAttribute("bids", bidHistory);
            model.addAttribute("item", item);
        } else {
            return null;
        }

        return "product.html";
    }

}
