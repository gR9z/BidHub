package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.UserDao;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        userDao.insert(user);
    }

    @Override
    public void createUserWithConfirmPassword(User user, String confirmPassword) {

        BusinessException businessException = new BusinessException();

        user.setCredit(0);
        user.setAdmin(false);

        if (arePasswordsMatching(user.getPassword(), confirmPassword, businessException)) {
            try {
                user.setPassword(this.passwordEncoder.encode(user.getPassword()));
                userDao.insert(user);

                if (request.getUserPrincipal() == null) {
                    request.login(user.getUsername(), confirmPassword);
                }

            } catch (BusinessException dalBusinessException) {
                throw dalBusinessException;
            } catch (ServletException e) {
                throw new RuntimeException("Login failed", e);
            }
        } else {
            throw businessException;
        }
    }

    @Override
    public void updateUser(User user) {
        userDao.update(user);
    }

    @Override
    public void updateUserByUsername(User user) {
        userDao.updateByUsername(user);
    }

    @Override
    public void removeUserById(int userId) {
        userDao.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optUser = userDao.selectUserByUsername(username);

        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return optUser.get();
    }

    @Override
    public UserDetails getUserByUserId(int userId) {
        return userDao.findById(userId).orElseThrow(() -> {
            BusinessException exception = new BusinessException();
            exception.addKey("User not found");
            return exception;
        });
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public int getTotalUserCount() {
        return userDao.count();
    }

    @Override
    public void debitUserCredit(Authentication authentication, Auction auction) throws BusinessException {
        BusinessException businessException = new BusinessException();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            businessException.addKey("Please log in to bib");
            throw businessException;
        }

        if (!(authentication.getPrincipal() instanceof User authenticatedUser)) {
            businessException.addKey("An unexpected error occurred. Please try again");
            throw businessException;
        }

        if (authenticatedUser.getUserId() != auction.getUserId()) {
            businessException.addKey("You do not have permission to perform this action");
            throw businessException;
        }

        int newCredit = authenticatedUser.getCredit() - auction.getBidAmount();
        if (newCredit < 0) {
            businessException.addKey("Insufficient credit to place this bid. Available credit: €" + authenticatedUser.getCredit());
            throw businessException;
        }

        authenticatedUser.setCredit(newCredit);
        userDao.update(authenticatedUser);
    }

    @Override
    public void refundUser(int userId, int refundAmount) {
        Optional<User> optionalUser = userDao.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setCredit(user.getCredit() + refundAmount);
            userDao.update(user);
            updateSessionUserCredit(user);
        }
    }


    public void deleteUser(User currentUser) {
        // TODO Problème de foreign key
        Optional<User> existingUser = userDao.findById(currentUser.getUserId());
        if (existingUser.isPresent()) {
            userDao.deleteById(currentUser.getUserId());
            updateSessionUserCredit(currentUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }



    private void updateSessionUserCredit(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User authenticatedUser) {
            authenticatedUser.setCredit(user.getCredit());
        }
    }

    public boolean arePasswordsMatching(String password, String confirmPassword, BusinessException businessException) {
        if (!password.equals(confirmPassword)) {
            businessException.addKey("Passwords does not match!");
            return false;
        }
        return true;
    }
}
