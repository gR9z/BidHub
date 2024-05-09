package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class UserProfileController {

    private UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String userProfile(
            Principal principal,
            Model model
    ) {
        User user = userService.getUsername(principal.getName());
        model.addAttribute("user", user);

        return "/user/profile-user";
    }

    @GetMapping("/profile/edit-profile")
    public String editProfile(
            Principal principal,
            Model model
    ) {
        User user = userService.getUsername(principal.getName());
        model.addAttribute("user", user);

        return "/user/edit-profile";
    }

    @PostMapping("/profile/edit-profile")
    public String saveEditProfile(
            @ModelAttribute User user,
            Principal principal
    ) {
        userService.editUserProfile(user);
        return "redirect:/profile";
    }

//    @PostMapping("/profile/delete")
//    public String deleteConfirm(
//            @ModelAttribute User user,
//            Principal principal,
//            HttpServletRequest request) {
//        User currentUser = userService.getUsername(principal.getName());
//        if (currentUser.getUsername().equals(user.getUsername())) {
//            userService.deleteUser(user);
//            HttpSession session = request.getSession(false);
//            if (session != null) {
//                session.invalidate();
//            }
//            //return "redirect:/security/login";
//            return "redirect:/login";
//        }
//        return "redirect:/profile";
//    }
//
//    @GetMapping("/profile/confirm-delete")
//    public String showDeleteConfirmation(Model model, Principal principal) {
//        User user = userService.getUsername(principal.getName());
//        model.addAttribute("user", user);
//        return "/user/confirm-delete"; // Page Thymeleaf pour confirmation
//    }

    @DeleteMapping("/profile/edit-profile")
    public ResponseEntity<String> deleteUser(Principal principal, HttpServletRequest request) {
        User currentUser = userService.getUsername(principal.getName());

        // Vérifier si l'utilisateur actuel est valide
        if (currentUser != null) {
            try {
                // Supprimer le compte utilisateur
                userService.deleteUser(currentUser);

                // Invalider la session
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }

                // Retourner une réponse de succès
                return ResponseEntity.ok("Account successfully deleted");
            } catch (Exception e) {
                // En cas d'erreur lors de la suppression du compte, retourner une réponse d'erreur
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting account");
            }
        } else {
            // Si l'utilisateur actuel n'est pas valide, retourner une réponse d'erreur
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }



//    @GetMapping("/confirm")
//    public String confirmDelete(Model model) {
//        model.addAttribute("showOverlay", true);
//        return "/login";
//    }

    @GetMapping("/error-profile")
    public String userError(
            @RequestParam(name = "error", required = false) String error,
            Model model
    ) {

        if (error != null) {
            String errorMessage = "Invalid user!";
            model.addAttribute("error", errorMessage);
            System.out.println(model);
        }

        return "/user/profile-user";
    }
}
