package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.UserDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private static String SELECT_BY_USERNAME = "SELECT * FROM users WHERE username = :username;";
    private static String INSERT = "INSERT INTO users (username, lastName, firstName, email, phone, street, zipCode, city, password, credit, isAdmin) " +
            "VALUES (:username, :lastName, :firstName, :email, :phone, :street, :zipCode, :city, :password, :credit, :isAdmin);";
    private static String SELECT_ALL = "SELECT * FROM users;";
    private static final String COUNT = "SELECT COUNT(*) AS count FROM users;";

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<User> selectUserByUsername(String username) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("username", username);
        try {
            User user = namedParameterJdbcTemplate.queryForObject(
                    SELECT_BY_USERNAME,
                    namedParameters,
                    new UserRowMapper()
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
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

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                INSERT,
                namedParameters,
                keyHolder
        );

        Number key = (Number) keyHolder.getKey();

        if(key != null) {
            user.setId(key.intValue());
        }
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new UserRowMapper());
    }

    @Override
    public int count() {
        return Optional.ofNullable(jdbcTemplate.queryForObject(COUNT, (rs, rowNum) -> rs.getInt("count")))
                .orElse(0);
    }

    public static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {

            return new User(
                    rs.getInt("userId"),
                    rs.getString("username"),
                    rs.getString("lastname"),
                    rs.getString("firstname"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("street"),
                    rs.getString("zipcode"),
                    rs.getString("city"),
                    rs.getString("password"),
                    rs.getInt("credit"),
                    rs.getBoolean("isAdmin")
            );
        }
    }
}
