package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.AuctionService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class AuctionController {

    // Déclaration des services nécessaires pour la gestion des enchères, des items et des utilisateurs.
    AuctionService auctionService;

    // Constructeur pour initialiser les services via l'injection de dépendances.
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    // Méthode pour créer une nouvelle enchère via une requête POST.
    @PostMapping("/products/{categorySlug}/{itemName}")
    public String createAuction(
            @ModelAttribute("auction") Auction auction, // Objet Auction lié à la requête.
            @ModelAttribute("item") Item item, // Objet Item lié à la requête.
            RedirectAttributes redirectAttributes, // Pour stocker les messages de succès ou d'erreur à rediriger.
            @PathVariable String categorySlug, // Slug de la catégorie du produit depuis l'URL.
            @PathVariable String itemName, // Nom de l'item depuis l'URL.
            Authentication authentication // Informations d'authentification de l'utilisateur.
    ) {
        // Construction de l'URL de redirection après la création de l'enchère.
        String redirectUrl = "redirect:/products/" + categorySlug + "/" + itemName + "?id=" + auction.getItemId();
        // Définition de la date de l'enchère à l'heure actuelle.
        auction.setAuctionDate(LocalDateTime.now());
        try {
            // Appel du service pour gérer la création de l'enchère.
            auctionService.handleAuctionCreation(auction, authentication);
            // Si la création réussit, ajout d'un message de succès à la redirection.
            redirectAttributes.addFlashAttribute("successMessage", "Auction created successfully!");
        } catch (BusinessException businessException) {
            // En cas d'erreur métier, ajout du message d'erreur et redirection vers l'URL.
            redirectAttributes.addFlashAttribute("errorMessage", businessException.getKeys());
            return redirectUrl; // Redirection avec message d'erreur.
        }
        // Redirection vers l'URL avec succès.
        return redirectUrl;
    }
}
