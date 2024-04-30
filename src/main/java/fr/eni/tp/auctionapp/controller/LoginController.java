package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
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
            Model model
    ) {
        if (error != null) {
            String errorMessage = "Invalid username or password!";
            model.addAttribute("error", errorMessage);
        }

        return "security/login";
    }

    @GetMapping("/create-account")
    public String showCreateAccountForm(Model model) {
        model.addAttribute("user", new User());

        return "user/create-account.html";
    }

    @PostMapping("/create-account")
    public String createUserAccount(
            User user,
            @RequestParam("passwordConfirm") String passwordConfirm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            System.out.println("ca passe");
            return "user/create-account.html";
        }

        try {
            user.setCredit(0);
            user.setAdmin(false);
            userService.createUser(user, passwordConfirm);

        } catch (BusinessException businessException) {

            businessException.getKeys().forEach(key -> {
                ObjectError error = new ObjectError("global", key);
                bindingResult.addError(error);
            });

            return "user/create-account.html";
        }

        return "user/profile-account.html";

    }
}
