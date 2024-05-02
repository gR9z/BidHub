package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    static Optional<User> selectUserByUsername(String username) {
        return null;
    }
    void createUser(User user);
    User getUsername(String username);
    void editUserProfile(User user);
    void deleteUser(User user);
}
