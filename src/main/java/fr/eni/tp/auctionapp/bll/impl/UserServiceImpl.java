package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.UserDao;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, HttpServletRequest request) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
    }

    @Override
    public void createUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        userDao.insertUser(user);
    }

    @Override
    public void createUser(User user, String confirmPassword) {

        BusinessException businessException = new BusinessException();

        if (arePasswordsMatching(user.getPassword(), confirmPassword, businessException)) {
          try {
              user.setPassword(this.passwordEncoder.encode(user.getPassword()));
              userDao.insertUser(user);
              request.login(user.getUsername(), confirmPassword);
          } catch(BusinessException dalBusinessException) {
              throw dalBusinessException;
          } catch (ServletException e) {
              throw new RuntimeException("Login failed", e);
          }
        } else {
            throw businessException;
        }

    }

    @Override
    public List<User> getUsers() {
        return userDao.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optUser = userDao.selectUserByUsername(username);

        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return optUser.get();
    }



    public boolean arePasswordsMatching(String password, String confirmPassword, BusinessException businessException) {

        if(!password.equals(confirmPassword)) {
            businessException.addKey("Passwords do not match!");
            return false;
        }

        return true;
    }
}
