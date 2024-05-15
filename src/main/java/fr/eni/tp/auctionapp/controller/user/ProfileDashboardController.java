package fr.eni.tp.auctionapp.controller.user;

import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bll.WithdrawalService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.bo.Withdrawal;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import fr.eni.tp.auctionapp.utils.URLUtils;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProfileDashboardController {

    ItemService itemService;
    CategoryService categoryService;
    WithdrawalService withdrawalService;
    UserService userService;

    public ProfileDashboardController(ItemService itemService, CategoryService categoryService, WithdrawalService withdrawalService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
        this.withdrawalService = withdrawalService;
    }

    @GetMapping("/profile/my-items")
    public String displayUserItems(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        if (authenticatedUser != null) {
            int userId = authenticatedUser.getUserId();

            List<Item> items = itemService.getByUserIdPaginated(userId, page, size);

            for (Item item : items) {
                Optional<Category> optionalCategory = categoryService.findCategoryById(item.getCategory().getCategoryId());
                if (optionalCategory.isPresent()) {
                    Category category = optionalCategory.get();
                    item.setCategory(category);
                }
            }

            Map<Integer, String> itemUrls = items.stream()
                    .collect(Collectors.toMap(Item::getItemId, item -> URLUtils.toFriendlyURL(item.getItemName()) + "?id=" + item.getItemId()));


            model.addAttribute("itemUrls", itemUrls);
            model.addAttribute("items", items);

            return "profile-dashboard/user-items.html";
        }
        return "home.html";
    }


    @GetMapping("/profile/my-items/{itemName}")
    public String displayItemDetails(
            @RequestParam("id") int itemId,
            @AuthenticationPrincipal User authenticatedUser,
            Model model
    ) {
        Optional<Item> optionalItem = itemService.findItemById(itemId);

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();

            int sellerId = item.getSeller().getUserId();
            if (authenticatedUser.getUserId() == sellerId) {

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

                String formattedAuctionStartingDate = item.getAuctionStartingDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                String formattedAuctionEndingDate = item.getAuctionEndingDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

                String itemUrl = URLUtils.toFriendlyURL(item.getItemName()) + "?id=" + item.getItemId();

                model.addAttribute("item", item);
                model.addAttribute("formattedAuctionStartingDate", formattedAuctionStartingDate);
                model.addAttribute("formattedAuctionEndingDate", formattedAuctionEndingDate);
                model.addAttribute("itemUrl", itemUrl);
                model.addAttribute("localDateTime", LocalDateTime.now());

                return "profile-dashboard/display-item.html";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/profile/my-items/edit/{itemName}")
    public String dislayEditForm(
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

            String formattedStartDate = formatDateTime(item.getAuctionStartingDate());
            String formattedEndDate = formatDateTime(item.getAuctionEndingDate());

            List<Category> categories = categoryService.getAllCategories();

            String itemUrl = URLUtils.toFriendlyURL(item.getItemName()) + "?id=" + item.getItemId();


            model.addAttribute("item", item);
            model.addAttribute("formattedEndDate", formattedEndDate);
            model.addAttribute("formattedStartDate", formattedStartDate);
            model.addAttribute("categories", categories);
            model.addAttribute("category", item.getCategory());
            model.addAttribute("seller", item.getSeller());
            model.addAttribute("withdrawal", item.getWithdrawal());
            model.addAttribute("itemUrl", itemUrl);
            model.addAttribute("sellerId", item.getSeller().getUserId());

            return "profile-dashboard/edit-item.html";
        }
        return "redirect:/profile/my-items/";
    }


    @PostMapping("/profile/my-items/edit/{itemName}")
    public String editItemDetails(
            @RequestParam("id") int itemId,
            @Valid Item item,
            RedirectAttributes redirectAttributes,
            Authentication authentication,
            int sellerId,
            Model model
    ) {

            try {
                itemService.handleItemUpdate(item, authentication, itemId, sellerId);

                redirectAttributes.addFlashAttribute("successMessage", "Item updated successfully!");
                return "redirect:/profile/my-items";
            } catch (BusinessException businessException) {

                List<Category> categories = categoryService.getAllCategories();

                model.addAttribute("categories", categories);
                model.addAttribute("item", item);

                redirectAttributes.addFlashAttribute("errorMessage", businessException.getKeys());
                return "profile-dashboard/edit-item.html";
            }
        }



    @PostMapping("/profile/my-items/delete/{itemName}")
    public String deleteItem(
            @RequestParam("id") int itemId,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        try {
            itemService.handleItemDeletion(itemId, authentication);
            redirectAttributes.addFlashAttribute("successMessage", "Item deleted successfully!");
            return "redirect:/profile/my-items";

        } catch (BusinessException businessException) {
            redirectAttributes.addFlashAttribute("errorMessage", businessException.getKeys());
            return "redirect:/profile/my-items/";
        }
    }

    public String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return dateTime.format(formatter);
    }

}
