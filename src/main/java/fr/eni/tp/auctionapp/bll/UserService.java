package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    static Optional<User> selectUserByUsername(String username) {
        return null;
    }
    void createUser(User user);
    void createUserWithConfirmPassword(User user, String confirmPassword);
    void updateUser(User user);
    void removeUserById(int id);

    UserDetails getUserByUserId(int userId);

    List<User> getAllUsers();
    int getTotalUserCount();

    void debitUserCredit(Authentication authentication, Auction auction) throws BusinessException;
    void refundUser(int userId, int refundAmount) throws BusinessException;
    User getUsername(String username);
    void editUserProfile(User user);
    void deleteUser(User user);
}
