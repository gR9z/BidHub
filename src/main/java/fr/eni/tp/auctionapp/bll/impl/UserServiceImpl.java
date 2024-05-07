package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final String SELECT_BY_USERNAME = "SELECT * FROM USERS WHERE username = ?;";

    @Autowired
    private final UserDao userDao;

    PasswordEncoder passwordEncoder;
    private JdbcOperations jdbcTemplate;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        userDao.insertUser(user);
    }

    @Override
    public User getUsername(String username) {
        return userDao.selectUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("fail: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> optUser = userDao.selectUserByUsername(username);

       if(optUser.isEmpty()) {
           throw new UsernameNotFoundException(username);
       }

       return optUser.get();
    }

    public Optional<User> selectUserByUsername(String username) {
        return userDao.selectUserByUsername(username);
    }

    public void editUserProfile(User user) {
        userDao.editUserProfile(user);
    }

    @Override
    public void deleteUser(User user) {
        userDao.deleteUser(user.getUsername());
    }

}
