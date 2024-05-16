package fr.eni.tp.auctionapp.controller.admin;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                             RedirectAttributes redirectAttributes
    ) {
        if (action.equals("delete")) {
            try {
                userService.removeUserById(userId);
                redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
                return "redirect:/admin/users";

            } catch (BusinessException businessException) {
                redirectAttributes.addFlashAttribute("errorMessage", businessException.getKeys());
                return "redirect:/admin/users";
            }
        }
        return "redirect:/admin/users";
    }
}