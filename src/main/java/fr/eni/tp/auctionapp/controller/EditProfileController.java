package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Objects;

@Controller
public class EditProfileController {

    private UserService userService;

    @Autowired
    public EditProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/error")
    public String userError(
            @RequestParam(name = "error", required = false) String error,
            Model model
    ) {
        if (error != null) {
            String errorMessage = "Invalid user!";
            model.addAttribute("error", errorMessage);
            System.out.println(model);
        }
        return "user/profile-user";
    }

    @GetMapping("/edit-profile")
    public String editProfileForm(
            Principal principal,
            Model model
    ) {
        User user = Objects.requireNonNull(UserService.selectUserByUsername(principal.getName())).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "user/edit-profile";
    }

    @PostMapping("/edit-profile")
    public String editSubmit(
            @ModelAttribute User user,
            @RequestParam("action") String action,
            Model model){

        if ("save".equals(action)) {

            userService.editUserProfile(user);
            return "redirect:/profile";

        } else if ("delete".equals(action)) {

            userService.deleteUser(user);
            return "redirect:/delete-account";

        } else if ("cancel".equals(action)) {
            return "redirect:/profile";
        }

        return "redirect:/error";
    }
}

