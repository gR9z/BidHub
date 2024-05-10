package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.AuctionService;
import fr.eni.tp.auctionapp.bo.Auction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuctionController {

    AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

//    @PostMapping("/products/{categorySlug}/{itemName}")
//    public String createAuction(
//            @ModelAttribute("auction") Auction auction,
//            RedirectAttributes redirectAttributes,
//            @PathVariable String categorySlug,
//            @PathVariable String itemName
//    ) {
//        auctionService.createAuction(auction);
//        redirectAttributes.addFlashAttribute("successMessage", "Auction created successfully!");
//        return "redirect:/products/" + categorySlug + "/" + itemName;
//    }

}
