package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
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


    @GetMapping("/profile")
    public String userProfile(
            Principal principal,
            Model model
    ) {
        User user = userService.getUsername(principal.getName());
        model.addAttribute("user", user);

        return "user/profile-user.html";
    }
}
