package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    void createUser(User user);
    void createUserWithConfirmPassword(User user, String confirmPassword);
    void updateUser(User user);
    void removeUserById(int id);
    List<User> getAllUsers();
    int getTotalUserCount();
}
