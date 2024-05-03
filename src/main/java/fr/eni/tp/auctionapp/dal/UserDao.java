package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> selectUserByUsername(String username);
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(int userId);

    List<User> findAll();
    List<User> findAllUsersPagination(int page, int size);

    int count();
}
