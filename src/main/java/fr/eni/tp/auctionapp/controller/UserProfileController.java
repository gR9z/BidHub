package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/profile/delete")
    public String deleteConfirm(
            @ModelAttribute User user,
            @RequestParam("action") String action,
            HttpServletRequest request,
            Model model){
        if ("yes".equals(action)) {
            userService.deleteUser(user);
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            //return "redirect:/security/login";
            return "redirect:/login";
        }
        return "redirect:/profile";
    }

    @GetMapping("/confirm")
    public String confirmDelete(Model model) {
        model.addAttribute("showOverlay", true);
        return "/login";
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

        return "/user/profile-user";
    }
}
