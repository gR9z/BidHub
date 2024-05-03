package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.bo.Withdrawal;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ItemController {

    private CategoryService categoryService;
    private ItemService itemService;
    private UserService userService;


    public ItemController(CategoryService categoryService, ItemService itemService, UserService userService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
        this.userService = userService;
    }


    @GetMapping("/create-item")
    public String displayCreateItemForm(Model model, @AuthenticationPrincipal User authenticatedUser) {

        if (authenticatedUser != null) {
            System.out.println(authenticatedUser.getStreet());

            Item item = new Item();
            Category category = new Category();
            item.setCategory(category);

            Withdrawal withdrawal = new Withdrawal();
            item.setWithdrawal(withdrawal);

            List<Category> categories = categoryService.readAll();

            model.addAttribute("item", item);
            model.addAttribute("categories", categories);
            model.addAttribute("withdrawal", withdrawal);
            model.addAttribute("authenticatedUser", authenticatedUser);

        return "item/createItem.html";
    }
        return "redirect:/";
    }



//    @PostMapping("/create-item")
//    public String createItem(
//            Principal principal,
//            @ModelAttribute("item") Item item,
//            BindingResult bindingResult,
//            Model model
//    ) {
//        if (principal != null) {
//            if (bindingResult.hasErrors()) {
//                List<Category> categories = categoryService.readAll();
//
//                model.addAttribute("categories", categories);
//                return "/create-item.html";
//            } else {
//                try {
//                    itemService.createItem(item);
//                    return "redirect:/";
//                } catch (BusinessException businessException) {
//                    List<Category> categories = categoryService.readAll();
//
//                    model.addAttribute("categories", categories);
//                    businessException.getKeys().forEach(key -> {
//                        ObjectError error = new ObjectError("globalError", key);
//                        bindingResult.addError(error);
//                    });
//                    return "/create-item.html";
//                }
//            }
//        }
//
//        return "redirect:/";
//    }


}
