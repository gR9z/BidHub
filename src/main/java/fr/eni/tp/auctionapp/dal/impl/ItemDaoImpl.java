package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.ItemDao;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemDaoImpl implements ItemDao {

    private static final String INSERT = "INSERT INTO ITEMS (itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId) VALUES (:itemName, :description, :auctionStartingDate, :auctionEndingDate, :startingPrice, :sellingPrice, :imageUrl, :userId, :categoryId);";
    private static final String SELECT_BY_ID = "SELECT itemId, itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId FROM ITEMS WHERE itemId = :itemId;";
    private static final String SELECT_ALL = "SELECT itemId, itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId FROM ITEMS;";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    public ItemDaoImpl(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            JdbcTemplate jdbcTemplate
    ) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(Item item) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("itemName", item.getItemName());
        namedParameters.addValue("description", item.getDescription());
        namedParameters.addValue("auctionStartingDate", item.getAuctionStartingDate());
        namedParameters.addValue("auctionEndingDate", item.getAuctionEndingDate());
        namedParameters.addValue("startingPrice", item.getStartingPrice());
        namedParameters.addValue("sellingPrice", item.getSellingPrice());
        namedParameters.addValue("imageUrl", item.getImageUrl());

        namedParameters.addValue("userId", item.getSeller().getUserId());
        namedParameters.addValue("categoryId", item.getCategory().getCategoryId());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                INSERT,
                namedParameters,
                keyHolder
        );

        Number key = (Number) keyHolder.getKey();

        if(key != null) {
            item.setItemId(key.intValue());
        }
    }

    @Override
    public Optional<Item> read(int id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("itemId", id);

        Item item = namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, namedParameters, new ItemRowMapper());
        return Optional.ofNullable(item);
    }

    @Override
    public void update(Item item) {

    }

    @Override
    public void delete(int itemId) {

    }

    @Override
    public List<Item> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new ItemRowMapper());
    }

    private static class ItemRowMapper implements RowMapper<Item> {
        @Override
        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            Item item = new Item();
            item.setItemId(rs.getInt("itemId"));
            item.setItemName(rs.getString("itemName"));
            item.setDescription(rs.getString("description"));

            LocalDateTime startingDateTime = rs.getTimestamp("auctionStartingDate").toLocalDateTime();
            LocalDateTime endingDateTime = rs.getTimestamp("auctionEndingDate").toLocalDateTime();

            item.setAuctionStartingDate(startingDateTime);
            item.setAuctionEndingDate(endingDateTime);
            item.setStartingPrice(rs.getInt("startingPrice"));
            item.setSellingPrice(rs.getInt("sellingPrice"));
            item.setImageUrl(rs.getString("imageUrl"));

            Category category = new Category();
            category.setCategoryId(rs.getInt("categoryId"));
            item.setCategory(category);

            User seller = new User();
            seller.setId(rs.getInt("userId"));
            item.setSeller(seller);

            return item;
        }
    }
}
