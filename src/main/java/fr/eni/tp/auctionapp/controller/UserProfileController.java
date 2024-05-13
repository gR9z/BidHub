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

        return "/account/profile-account";
    }

    @GetMapping("/profile/edit-profile")
    public String editProfile(
            Principal principal,
            Model model
    ) {
        User user = userService.getUsername(principal.getName());
        model.addAttribute("user", user);

        return "/account/edit-account";
    }

    @PostMapping("/profile/edit-profile")
    public String saveEditProfile(
            @ModelAttribute User user,
            Principal principal
    ) {
        userService.editUserProfile(user);
        return "redirect:/profile";
    }

    @DeleteMapping("/profile/edit-profile")
    public ResponseEntity<String> deleteUser(@RequestParam("username") String username, HttpServletRequest request) {
        User currentUser = userService.getUsername(username);

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
            Model model
    ) {

        if (error != null) {
            String errorMessage = "Invalid user!";
            model.addAttribute("error", errorMessage);
            System.out.println(model);
        }

        return "/account/profile-account";
    }
}
