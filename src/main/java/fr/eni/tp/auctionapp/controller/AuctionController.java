package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.AuctionService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class AuctionController {

    AuctionService auctionService;
    ItemService itemService;
    UserService userService;

    public AuctionController(AuctionService auctionService, ItemService itemService, UserService userService) {
        this.auctionService = auctionService;
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping("/products/{categorySlug}/{itemName}")
    public String createAuction(
            @ModelAttribute("auction") Auction auction,
            @ModelAttribute("item") Item item,
            RedirectAttributes redirectAttributes,
            @PathVariable String categorySlug,
            @PathVariable String itemName,
            Authentication authentication
    ) {
        String redirectUrl = "redirect:/products/" + categorySlug + "/" + itemName + "?id=" + auction.getItemId();
        auction.setAuctionDate(LocalDateTime.now());

        try {
            auctionService.handleAuctionCreation(auction, authentication);

            redirectAttributes.addFlashAttribute("successMessage", "Auction created successfully!");
        } catch (BusinessException businessException) {
            redirectAttributes.addFlashAttribute("errorMessage", businessException.getKeys());
            return redirectUrl;
        }

        return redirectUrl;
    }
}
