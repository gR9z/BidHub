package fr.eni.tp.auctionapp.controller.admin;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminDashboardUserController {

    UserService userService;

    public AdminDashboardUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String displayAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        int userCount = users.size();

        model.addAttribute("users", users);
        model.addAttribute("userCount", userCount);
        return "admin/user/users";
    }

    @PostMapping("/users")
    public String manageUser(@RequestParam("id") int userId,
                             @RequestParam("action") String action,
                             Model model) {
        System.out.println(userId);
        System.out.println(action);

        if (action.equals("delete")) {
            try {
                userService.removeUserById(userId);
                model.addAttribute("successMessage", "User deleted successfully");
                return "/users";

            } catch (BusinessException businessException) {
                businessException.getKeys().forEach(key -> {
                    ObjectError error = new ObjectError("globalError", key);
                    model.addAttribute("org.springframework.validation.BindingResult.model", error);
                });
                return "/users";
            }
        }
        return "/users";
    }
}