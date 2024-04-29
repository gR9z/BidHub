package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.UserDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private String SELECT_BY_USERNAME = "SELECT * FROM users WHERE username = ?;";
    private String INSERT = "INSERT INTO users (username, lastName, firstName, email, phone, street, zipCode, city, password, credit, isAdmin) " +
            "VALUES (:username, :lastName, :firstName, :email, :phone, :street, :zipCode, :city, :password, :credit, :isAdmin);";

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<User> selectUserByUsername(String username) {
        User user = this.jdbcTemplate.queryForObject(SELECT_BY_USERNAME, new BeanPropertyRowMapper<>(User.class), username);
        return Optional.ofNullable(user);
    }

    @Override
    public void insertUser(User user) {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("username", user.getUsername());
        namedParameters.addValue("lastName", user.getLastName());
        namedParameters.addValue("firstName", user.getFirstName());
        namedParameters.addValue("email", user.getEmail());
        namedParameters.addValue("phone", user.getPhone());
        namedParameters.addValue("street", user.getStreet());
        namedParameters.addValue("zipCode", user.getZipCode());
        namedParameters.addValue("city", user.getCity());
        namedParameters.addValue("password", user.getPassword());
        namedParameters.addValue("credit", user.getCredit());
        namedParameters.addValue("isAdmin", user.isAdmin());

        namedParameterJdbcTemplate.update(
                INSERT,
                namedParameters
        );
    }
}
