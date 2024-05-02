package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> selectUserByUsername(String username);

    void editUserProfile(User user, String originalUsername);

    void insertUser(User user);

    void deleteUser(String username);
}
