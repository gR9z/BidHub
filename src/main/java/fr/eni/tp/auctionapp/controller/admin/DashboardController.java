package fr.eni.tp.auctionapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin")
    public String dashboard() {
        return "admin/admin-dashboard.html";
    }
}
