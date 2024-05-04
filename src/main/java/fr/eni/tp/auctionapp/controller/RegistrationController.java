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
public class RegistrationController {

    UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create-account")
    public String showCreateAccountForm(Model model) {
        model.addAttribute("user", new User());

        return "account/create-account.html";
    }

    @PostMapping("/create-account")
    public String createUserAccount(
            User user,
            @RequestParam("passwordConfirm") String passwordConfirm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "account/create-account.html";
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

            return "account/create-account.html";
        }

        return "account/profile-account.html";

    }
}
