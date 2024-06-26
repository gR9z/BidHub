package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(name = "error", required = false) String error,
            Model model,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        if (currentUser != null) {
            return "redirect:/profile";
        }

        if (error != null) {
            String errorMessage = "Invalid username or password!";
            model.addAttribute("error", errorMessage);
        }

        model.addAttribute("isLoginPage", true);

        return "security/login";
    }


}
