package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.UserDao;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    PasswordEncoder passwordEncoder;

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
    public void createUser(User user, String confirmPassword) {

        BusinessException businessException = new BusinessException();

        if (arePasswordsMatching(user.getPassword(), confirmPassword, businessException)) {
          try {
              user.setPassword(this.passwordEncoder.encode(user.getPassword()));
              userDao.insertUser(user);
          } catch(BusinessException dalBusinessException) {
              throw dalBusinessException;
          }
        } else {
            throw businessException;
        }

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
