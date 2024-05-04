package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.Withdrawal;
import fr.eni.tp.auctionapp.dal.WithdrawalDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class WithdrawalDAOImpl implements WithdrawalDAO {

    private static final String INSERT_INTO = "INSERT INTO WITHDRAWALS (itemId, street, zipCode, city) VALUES (:itemId, :street, :zipCode, :city)";
    private static final String SELECT_BY_ITEM_ID = "SELECT itemId, street, zipCode, city FROM WITHDRAWALS WHERE itemId = : :id";
    private static final String UPDATE = "UPDATE WITHDRAWALS SET itemId = :itemId, street = :street, zipCode = :zipCode WHERE itemId = :itemId";

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public WithdrawalDAOImpl(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    @Override
    public void insert(Withdrawal withdrawal) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("itemId", withdrawal.getItem().getItemId());
        namedParameters.addValue("street", withdrawal.getStreet());
        namedParameters.addValue("zipCode", withdrawal.getZipCode());
        namedParameters.addValue("city", withdrawal.getCity());

        namedParameterJdbcTemplate.update(
                INSERT_INTO,
                namedParameters
        );
    }

    @Override
    public Withdrawal getWithdrawalById(int itemId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("itemId", itemId);

        Withdrawal withdrawal = namedParameterJdbcTemplate.queryForObject(
                SELECT_BY_ITEM_ID,
                namedParameters,
                new WithdrawalRowMapper()
        );

        return withdrawal;
    }

    @Override
    public void update(Withdrawal withdrawal) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("itemId", withdrawal.getItem().getItemId());
        namedParameters.addValue("street", withdrawal.getStreet());
        namedParameters.addValue("zipCode", withdrawal.getZipCode());
        namedParameters.addValue("city", withdrawal.getCity());

        namedParameterJdbcTemplate.update(UPDATE, namedParameters);
    }


    private class WithdrawalRowMapper implements RowMapper<Withdrawal> {
        @Override
        public Withdrawal mapRow(ResultSet rs, int rowNum) throws SQLException {
            Withdrawal withdrawal = new Withdrawal();
            withdrawal.setItemId(rs.getInt("itemId"));
            withdrawal.setStreet(rs.getString("street"));
            withdrawal.setZipCode(rs.getString("zipCode"));
            withdrawal.setCity(rs.getString("city"));

            return withdrawal;
        }

    }
}
