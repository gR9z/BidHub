package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.UserDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private String SELECT_BY_USERNAME = "SELECT * FROM USERS WHERE username = ?;";
    private String INSERT = "INSERT INTO users (username, lastName, firstName, email, phone, street, zipCode, city, password, credit, isAdmin) " +
            "VALUES (:username, :lastName, :firstName, :email, :phone, :street, :zipCode, :city, :password, :credit, :isAdmin);";
    private String EDIT_VALUES_BY_USERNAME = "UPDATE USERS\n" +
            "SET username = :username,\n" +
            "    lastName = :lasName,\n" +
            "    firstName = :firstName,\n" +
            "    email = :email,\n" +
            "    phone = :phone,\n" +
            "    street = :street,\n" +
            "    zipCode = :zipCode,\n" +
            "    city = :city,\n" +
            "    password = :password,\n" +
            "    credit = :credit,\n" +
            "    isAdmin = :isAdmin" +
            "WHERE username = ?;";


    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<User> selectUserByUsername(String username) {
        User user = this.jdbcTemplate.queryForObject(SELECT_BY_USERNAME, new UserRowMapper(), username);
        return Optional.ofNullable(user);
    }

    @Override
    public void editUserProfile(User user, String originalUsername) {
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
        namedParameters.addValue("originalUsername", originalUsername);

        namedParameterJdbcTemplate.update(EDIT_VALUES_BY_USERNAME, namedParameters);
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



    public class UserRowMapper implements RowMapper<User> {
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
