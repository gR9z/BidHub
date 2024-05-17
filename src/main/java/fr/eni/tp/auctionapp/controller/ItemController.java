package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.ItemService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class ItemController {

    private final CategoryService categoryService;
    private final ItemService itemService;

    public ItemController(CategoryService categoryService, ItemService itemService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
    }

    @GetMapping("/create-item")
    public String displayCreateItemForm(Model model, @AuthenticationPrincipal User authenticatedUser) {

        if (authenticatedUser != null) {

            Item item = new Item();

            // TODO à faire dans TL
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String formattedDateTime = now.format(formatter);

            item.setAuctionStartingDate(LocalDateTime.parse(formattedDateTime, formatter));
            item.setAuctionEndingDate(LocalDateTime.now().plusDays(7));

            Category category = new Category();
            Withdrawal withdrawal = new Withdrawal();

            item.setCategory(category);
            item.setSeller(authenticatedUser);

            withdrawal.setStreet(authenticatedUser.getStreet());
            withdrawal.setZipCode(authenticatedUser.getZipCode());
            withdrawal.setCity(authenticatedUser.getCity());

            item.setWithdrawal(withdrawal);

            List<Category> categories = categoryService.getAllCategories();

            model.addAttribute("item", item);
            model.addAttribute("categories", categories);
            model.addAttribute("withdrawal", withdrawal);
            model.addAttribute("authenticatedUser", authenticatedUser);

            return "item/create-item.html";
        }
        return "redirect:/";
    }

    @PostMapping("/create-item")
    public String createItem(
            @AuthenticationPrincipal User authenticatedUser,
            @Valid Item item,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            model.addAttribute("item", item);
            return "item/create-item.html";
        } else {
            try {
                item.setSeller(authenticatedUser);

                itemService.createItem(item);
                return "redirect:/products/" + URLUtils.toFriendlyURL(item.getCategory().getLabel())
                        + "/" + URLUtils.toFriendlyURL(item.getItemName())
                        + "?id=" + item.getItemId();
            } catch (BusinessException businessException) {

                List<Category> categories = categoryService.getAllCategories();

                model.addAttribute("categories", categories);
                model.addAttribute("item", item);

                businessException.getKeys().forEach(key -> {
                    ObjectError error = new ObjectError("globalError", key);
                    bindingResult.addError(error);
                });

                return "item/create-item.html";
            }
        }
    }

    public String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return dateTime.format(formatter);
    }


}

