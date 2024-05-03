package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.dal.AuctionDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository
public class AuctionDaoImpl implements AuctionDao {

    private static String INSERT = "INSERT INTO AUCTIONS (userId, itemId, bidAmount) VALUES (:userId, :itemId, :bidAmount);";
    private static final String COUNT = "SELECT COUNT(*) AS count FROM auctions;";

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AuctionDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void insert(Item item, int bidAmount) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", item.getBuyer().getUserId());
        params.addValue("itemId", item.getItemId());
        params.addValue("bidAmount", bidAmount);
    }

    @Override
    public Auction read(int id) {
        return null;
    }

    public int count() {
        return Optional.ofNullable(jdbcTemplate.queryForObject(COUNT, (rs, rowNum) -> rs.getInt("count")))
                .orElse(0);
    }

}
