package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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


    @RequestMapping("/profile")
    public String editProfileForm(
            Principal principal,
            Model model
    ) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);

        return "account/profile";
    }

    @PostMapping("/profile")
    public String handleProfileAction(
            @ModelAttribute User user,
            @RequestParam(name = "id", required = true) Integer id,
            @RequestParam(name = "action", required = true) String action,
            @AuthenticationPrincipal User authenticatedUser,
            Model model
    ) {
        if ("edit".equals(action)) {
            user.setPassword(authenticatedUser.getPassword());
            user.setCredit(authenticatedUser.getCredit());

            userService.updateUserByUsername(user);
            model.addAttribute("successMessage", "Your profile has been updated");
        } else if ("delete".equals(action) && id != null) {
            try {
                userService.removeUserById(id);
                return "redirect:/";
            } catch (Exception e) {
                UserDetails existingUser = userService.getUserByUserId(user.getUserId());
                model.addAttribute("user", existingUser);
                model.addAttribute("errorMessage", "An error occurred while deleting your account. Please try again.");
            }
        } else {
            throw new IllegalArgumentException("Invalid action!");
        }
        return "account/profile";
    }
}
