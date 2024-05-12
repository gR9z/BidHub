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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserDashboardController {

    ItemService itemService;
    CategoryService categoryService;
    WithdrawalService withdrawalService;
    UserService userService;

    public UserDashboardController(ItemService itemService, CategoryService categoryService, WithdrawalService withdrawalService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
        this.withdrawalService = withdrawalService;
    }

    @GetMapping("/user/my-items")
    public String displayUserItems(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        if (authenticatedUser != null) {
            int userId = authenticatedUser.getUserId();

            List<Item> items = itemService.getByUserIdPaginated(userId, page, size);

            for(Item item : items) {
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

            return "user-dashboard/user-items.html";
        }
        return "home.html";
    }




    @GetMapping("/user/my-items/{itemName}")
    public String displayItemDetails(
            @RequestParam("id") int itemId,
            @AuthenticationPrincipal User authenticatedUser,
            Model model
    ) {
        Optional<Item> optionalItem = itemService.findItemById(itemId);

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();

            int sellerId = item.getSeller().getUserId();
            if(authenticatedUser.getUserId() == sellerId){

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

//            String itemUrl = URLUtils.toFriendlyURL(item.getItemName()) + "?id=" + item.getItemId();

//            System.out.println(itemUrl);

            model.addAttribute("item", item);
            model.addAttribute("formattedAuctionStartingDate", formattedAuctionStartingDate);
            model.addAttribute("formattedAuctionEndingDate", formattedAuctionEndingDate);
//            model.addAttribute("itemUrl", itemUrl);

            return "user-dashboard/display-item.html";
            }
        }
        return "redirect:/";
    }


    @GetMapping("/user/my-items/{itemName}/edit")
    public String dislayEditForm(
            @RequestParam("id") int itemId,
            @AuthenticationPrincipal User authenticatedUser,
            Model model
    ) {
        Optional<Item> optionalItem = itemService.findItemById(itemId);

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();

            int sellerId = item.getSeller().getUserId();
            if(authenticatedUser.getUserId() == sellerId & LocalDateTime.now().isBefore(item.getAuctionStartingDate())) {

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

                List<Category> categories = categoryService.getAllCategories();

                model.addAttribute("item", item);
                model.addAttribute("formattedAuctionStartingDate", formattedAuctionStartingDate);
                model.addAttribute("formattedAuctionEndingDate", formattedAuctionEndingDate);
                model.addAttribute("categories", categories);
                model.addAttribute("category", item.getCategory());
                model.addAttribute("seller", item.getSeller());
                model.addAttribute("withdrawal", item.getWithdrawal());

                return "user-dashboard/edit-item.html";
            }
        }
        return "redirect:/";
    }


    @PostMapping("/user/my-items/{itemName}/edit")
    public String editItemDetails(
            @AuthenticationPrincipal User authenticatedUser,
            @Valid Item item,
            BindingResult bindingResult,
            Model model
    ){

        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            model.addAttribute("item", item);
            return "user-dashboard/edit-item.html";
        } else {
            try {
                item.setSeller(authenticatedUser);
                itemService.updateItem(item);
                withdrawalService.updateWithdrawal(item.getWithdrawal());
                return "redirect:/user/my-items/";
            } catch (BusinessException businessException) {

                List<Category> categories = categoryService.getAllCategories();

                model.addAttribute("categories", categories);
                model.addAttribute("item", item);

                businessException.getKeys().forEach(key -> {
                    ObjectError error = new ObjectError("globalError", key);
                    bindingResult.addError(error);
                });

                return "user-dashboard/edit-item.html";
            }
        }
    }


    @DeleteMapping("/user/my-items/{itemName}/delete")
    public String deleteItem(
            @RequestParam("id") int itemId,
            @AuthenticationPrincipal User authenticatedUser,
            Model model
    ) {
        Optional<Item> optionalItem = itemService.findItemById(itemId);

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();

            int sellerId = item.getSeller().getUserId();
            if(authenticatedUser.getUserId() == sellerId & LocalDateTime.now().isBefore(item.getAuctionStartingDate())) {

                itemService.removeItemById(itemId);

                return "user-dashboard/user-items.html";
            }
        }
        return "user-dashboard/user-items.html";
    }

}
