package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.UserDao;
import org.springframework.dao.EmptyResultDataAccessException;
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
    private static String SELECT_BY_ID = "SELECT * FROM users WHERE userId = :userId;";
    private static String INSERT = "INSERT INTO users (username, lastName, firstName, email, phone, street, zipCode, city, password, credit, isAdmin) " +
            "VALUES (:username, :lastName, :firstName, :email, :phone, :street, :zipCode, :city, :password, :credit, :isAdmin);";
    private static String UPDATE_BY_ID = "UPDATE users SET username = :username, lastName = :lastName, firstName = :firstName, email = :email, phone = :phone, street = :street, zipCode = :zipCode, city = :city, credit = :credit, isAdmin = :isAdmin WHERE userId = :userId;";
    private static String DELETE_BY_ID = "DELETE FROM users WHERE userId = :userId;";
    private static String SELECT_ALL = "SELECT * FROM users;";
    private static final String SELECT_ALL_PAGINATED = "SELECT * FROM USERS ORDER BY userId OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY;";
    private static final String COUNT = "SELECT COUNT(*) AS count FROM users;";
    private String EDIT_VALUES_BY_USERNAME = "UPDATE USERS\n" +
            "SET username = :username,\n" +
            "    lastName = :lastName,\n" +
            "    firstName = :firstName,\n" +
            "    email = :email,\n" +
            "    phone = :phone,\n" +
            "    street = :street,\n" +
            "    zipCode = :zipCode,\n" +
            "    city = :city,\n" +
            "    password = :password,\n" +
            "    credit = :credit,\n" +
            "    isAdmin = :isAdmin\n" +
            "WHERE username = :username;";
    private String DELETE_BY_USERNAME = "DELETE FROM USERS WHERE username = ?";


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
    public void editUserProfile(User user) {
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

        namedParameterJdbcTemplate.update(EDIT_VALUES_BY_USERNAME, namedParameters);
    }

    @Override
    public Optional<User> findById(int userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);

        try {
            User user = namedParameterJdbcTemplate.queryForObject(
                    SELECT_BY_ID,
                    namedParameters,
                    new UserRowMapper()
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void insert(User user) {

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

        if (key != null) {
            user.setId(key.intValue());
        }
    }

    @Override
    public void update(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", user.getUserId());
        params.addValue("username", user.getUsername());
        params.addValue("lastName", user.getLastName());
        params.addValue("firstName", user.getFirstName());
        params.addValue("email", user.getEmail());
        params.addValue("phone", user.getPhone());
        params.addValue("street", user.getStreet());
        params.addValue("zipCode", user.getZipCode());
        params.addValue("city", user.getCity());
        params.addValue("password", user.getPassword());
        params.addValue("credit", user.getCredit());
        params.addValue("isAdmin", user.isAdmin());

        namedParameterJdbcTemplate.update(UPDATE_BY_ID, params);
    }

    @Override
    public void deleteById(int userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        namedParameterJdbcTemplate.update(DELETE_BY_ID, params);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new UserRowMapper());
    }

    @Override
    public List<User> findAllPagination(int page, int size) {
        if (page < 1) page = 1;
        int offset = (page - 1) * size;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limit", size);
        params.addValue("offset", offset);

        return namedParameterJdbcTemplate.query(
                SELECT_ALL_PAGINATED,
                params,
                new UserRowMapper()
        );
    }

    @Override
    public int count() {
        return Optional.ofNullable(jdbcTemplate.queryForObject(COUNT, (rs, rowNum) -> rs.getInt("count")))
                .orElse(0);
    }

    public void deleteUser(String username) {
        jdbcTemplate.update(DELETE_BY_USERNAME, username);
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
