package fr.eni.tp.auctionapp.controller;

import fr.eni.tp.auctionapp.bll.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EditProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/edit-profile")
    public String editProfile(Model model) {
        model.addAttribute("editProfile",new EditProfile());
        return "edit";
    }

    @PostMapping("/edit-profile")
    public String editSubmit(
            @ModelAttribute EditProfile editProfile,
            Model model){
        userService.editUserProfile(editProfile.toUser(), editProfile.getOriginalUsername());
        model.addAttribute("editProfile", editProfile);
        return "edit";
    }
}
