package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.UserDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private String SELECT_BY_USERNAME = "SELECT * FROM users WHERE username = ?;";
    private String INSERT = "INSERT INTO users (email, username, password) VALUES (?, ?, ?);";

    private JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> selectUserByUsername(String username) {
        User user = this.jdbcTemplate.queryForObject(SELECT_BY_USERNAME, new BeanPropertyRowMapper<>(User.class), username);
        return Optional.ofNullable(user);
    }

    @Override
    public void insertUser(User user) {
        this.jdbcTemplate.update(
                INSERT,
                user.getEmail(),
                user.getUsername(),
                user.getPassword()
        );
    }
}
