package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.ItemDao;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemDaoImpl implements ItemDao {

    private static final String INSERT = "INSERT INTO ITEMS (itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId) VALUES (:itemName, :description, :auctionStartingDate, :auctionEndingDate, :startingPrice, :sellingPrice, :imageUrl, :userId, :categoryId);";
    private static final String SELECT_BY_ID = "SELECT itemId, itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId FROM ITEMS WHERE itemId = :itemId;";
    private static final String UPDATE_BY_ID = "UPDATE ITEMS SET itemName = :itemName, description = :description, auctionStartingDate = :auctionStartingDate, auctionEndingDate = :auctionEndingDate, startingPrice = :startingPrice, sellingPrice = :sellingPrice, imageUrl = :imageUrl, userId = :userId, categoryId = :categoryId WHERE itemId = :itemId;";
    private static final String DELETE_BY_ID = "DELETE FROM ITEMS WHERE itemId = :itemId;";
    private static final String SELECT_ALL = "SELECT itemId, itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId FROM ITEMS;";
    private static final String SELECT_ALL_PAGINATED = "SELECT itemId, itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId FROM ITEMS ORDER BY auctionStartingDate OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY;";
    private static final String SELECT_ALL_ITEMS_FROM_USERID_PAGINATED = "SELECT itemId, itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId FROM ITEMS WHERE userId = :userId ORDER BY auctionStartingDate OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY;";
    private static final String COUNT_ITEM_BY_USER_ID = "SELECT COUNT(*) AS count FROM Items WHERE userId = :userId;";
    private static final String COUNT = "SELECT COUNT(*) AS count FROM Items;";

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

        if (key != null) {
            item.setItemId(key.intValue());
        }
    }

    @Override
    public Optional<Item> read(int id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("itemId", id);
        try {
            Item item = namedParameterJdbcTemplate.queryForObject(
                    SELECT_BY_ID,
                    namedParameters,
                    new ItemRowMapper()
            );
            return Optional.ofNullable(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Item item) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("itemId", item.getItemId());
        params.addValue("itemName", item.getItemName());
        params.addValue("description", item.getDescription());
        params.addValue("auctionStartingDate", item.getAuctionStartingDate());
        params.addValue("auctionEndingDate", item.getAuctionEndingDate());
        params.addValue("startingPrice", item.getStartingPrice());
        params.addValue("sellingPrice", item.getSellingPrice());
        params.addValue("imageUrl", item.getImageUrl());
        params.addValue("userId", item.getSeller().getUserId());
        params.addValue("categoryId", item.getCategory().getCategoryId());

        namedParameterJdbcTemplate.update(UPDATE_BY_ID, params);
    }

    @Override
    public void delete(int itemId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("itemId", itemId);
        namedParameterJdbcTemplate.update(DELETE_BY_ID, params);
    }

    @Override
    public List<Item> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new ItemRowMapper());
    }

    public List<Item> findAllItemsPaginated(int page, int size) {

        int offset = (page - 1) * size;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limit", size);
        params.addValue("offset", offset);

        return namedParameterJdbcTemplate.query(
                SELECT_ALL_PAGINATED,
                params,
                new ItemRowMapper()
        );
    }

    public List<Item> findAllItemsByUserIdPaginated(int userId, int page, int size) {

        int offset = (page - 1) * size;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("limit", size);
        params.addValue("offset", offset);

        return namedParameterJdbcTemplate.query(
                SELECT_ALL_ITEMS_FROM_USERID_PAGINATED,
                params,
                new ItemRowMapper()
        );
    }

    public int countItemsByUserId(int userId) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("userId", userId);
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(COUNT_ITEM_BY_USER_ID, params, Integer.class)).orElse(0);
    }

    public int count() {
        return Optional.ofNullable(jdbcTemplate.queryForObject(COUNT, (rs, rowNum) -> rs.getInt("count")))
                .orElse(0);
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
