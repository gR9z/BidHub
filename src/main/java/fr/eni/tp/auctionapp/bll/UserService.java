package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    void createUser(User user);
    void createUser(User user, String confirmPassword);

    List<User> getUsers();
}
