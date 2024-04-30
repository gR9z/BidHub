package fr.eni.tp.auctionapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(
            @RequestParam(name = "error", required = false) String error,
            Model model
    ) {
        if (error != null) {
            String errorMessage = "Invalid username or password!";
            model.addAttribute("error", errorMessage);
            System.out.println(model);
        }

        return "security/login";
    }
}
