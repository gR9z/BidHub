package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        User user = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);

        return "account/profile-account";
    }

    @RequestMapping("/account/edit-account")
    public String editProfile(
            Principal principal,
            Model model
    ) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);

        return "account/edit-account";
    }

    @PostMapping("/profile")
    public String saveEditProfile(
            @ModelAttribute User user,
            Principal principal
    ) {
        userService.updateUserByUsername(user);
        return "redirect:profile";
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteUser(@RequestParam("username") String username, HttpServletRequest request) {
        User currentUser = (User) userService.loadUserByUsername(username);

        if (currentUser != null) {
            try {
                userService.deleteUser(currentUser);

                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }

                return ResponseEntity.ok("Account successfully deleted");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting account");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/error-profile")
    public String userError(
            @RequestParam(name = "error", required = false) String error,
            Model model,
            Principal principal
    ) {

        if (error != null) {
            String errorMessage = "Invalid user!";
            model.addAttribute("error", errorMessage);
            System.out.println(model);
        }

        User user = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);

        return "/account/profile-account";
    }
}
