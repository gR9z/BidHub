package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> selectUserByUsername(String username);
    void insertUser(User user);

    List<User> findAll();
}
