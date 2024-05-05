package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void insert(User user);
    Optional<User> selectUserByUsername(String username);
    void update(User user);
    void deleteById(int userId);

    List<User> findAll();
    List<User> findAllPagination(int page, int size);

    int count();
}
