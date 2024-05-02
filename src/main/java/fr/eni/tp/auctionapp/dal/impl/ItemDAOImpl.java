package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.ItemDAO;
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

@Repository
public class ItemDAOImpl implements ItemDAO {

    private static final String INSERT = "INSERT INTO ITEMS (itemName, description, categoryId, startingPrice, auctionStartingDate, auctionEndingDate, userId) VALUES (:itemName, :description, :categoryId, :startingPrice, :auctionStartingDate, :auctionEndingDate, :userId)";
    private static final String SELECT_BY_ID = "SELECT it.itemId, itemName, description, label, it.categoryID, sellingPrice, startingPrice, auctionStartingDate, auctionEndingDate, wth.street, wth.zipCode, wth.city, us.username FROM ITEMS AS it" +
            "   INNER JOIN CATEGORIES AS cat ON it.categoryId = cat.categoryId" +
            "   INNER JOIN WITHDRAWALS AS wth ON it.itemId = wth.itemId" +
            "   INNER JOIN USERS AS us ON it.userId = us.userId" +
            "   WHERE it.itemId = :id";
    private static final String UPDATE = "UPDATE ITEMS SET itemName = :name, description = :description, categoryId = :categoryId, startingPrice = :startingPrice, auctionStartingDate = :auctionStartingDate, auctionEndingDate = :auctionEndingDate WHERE itemId = :itemId";
    private static final String DELETE = "DELETE FROM ITEMS WHERE itemId = :itemId";
    private static final String SELECT_ALL = "SELECT it.itemName, startingPrice, auctionEndingDate, us.username, cat.categoryId, cat.label FROM ITEMS AS it" +
            "   INNER JOIN CATEGORIES AS cat ON it.categoryId = cat.categoryId" +
            "   INNER JOIN USERS AS us ON it.userId = us.userId";


    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    public ItemDAOImpl(
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
        namedParameters.addValue("categoryId", item.getCategory().getCategoryId());
        namedParameters.addValue("startingPrice", item.getStartingPrice());
        namedParameters.addValue("userId", item.getSeller().getUserId());
        namedParameters.addValue("auctionStartingDate", item.getAuctionStartingDate());
        namedParameters.addValue("auctionEndingDate", item.getAuctionEndingDate());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                INSERT,
                namedParameters
        );

        if (keyHolder.getKey() != null) {
            item.setItemId(keyHolder.getKey().intValue());
        }

    }

    @Override
    public Item read(int id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);

        Item item = namedParameterJdbcTemplate.queryForObject(
                SELECT_BY_ID,
                namedParameters,
                new ItemRowMapper()
        );

        return item;
    }

    @Override
    public void update(Item item) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("itemId", item.getItemId());
        namedParameters.addValue("itemName", item.getItemName());
        namedParameters.addValue("description", item.getDescription());
        namedParameters.addValue("categoryId", item.getCategory().getCategoryId());
        namedParameters.addValue("startingPrice", item.getStartingPrice());
        namedParameters.addValue("auctionStartingDate", item.getAuctionStartingDate());
        namedParameters.addValue("auctionEndingDate", item.getAuctionEndingDate());

        namedParameterJdbcTemplate.update(UPDATE, namedParameters);
    }

    @Override
    public void delete(int itemId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", itemId);

        namedParameterJdbcTemplate.update(DELETE, namedParameters);
    }

    @Override
    public List<Item> findAll() {
        List<Item> items = jdbcTemplate.query(
                SELECT_ALL,
                new AllItemRowMapper()
        );

        return items;
    }




    private class ItemRowMapper implements RowMapper<Item> {
        @Override
        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            Item item = new Item();
            item.setItemId(rs.getInt("itemId"));
            item.setItemName(rs.getString("itemName"));
            item.setDescription(rs.getString("description"));

            Category category = new Category();
            category.setCategoryId(rs.getInt("categoryId"));
            category.setLabel(rs.getString("label"));
            item.setCategory(category);

            item.setSellingPrice(rs.getInt("sellingPrice"));
            item.setStartingPrice(rs.getInt("startingPrice"));

            LocalDateTime startingDateTime = rs.getTimestamp("auctionStartingDate").toLocalDateTime();
            LocalDateTime endingDateTime = rs.getTimestamp("auctionEndingDate").toLocalDateTime();
//            item.setAuctionStartingDate(rs.getDate("auctionStartingDate"));
//            item.setAuctionEndingDate(rs.getDate("auctionEndingDate"));
            item.setAuctionEndingDate(startingDateTime);
            item.setAuctionEndingDate(endingDateTime);

            User seller = new User();
            seller.setUsername(rs.getString("username"));
            item.setSeller(seller);


            return item;
        }
    }

    private class AllItemRowMapper implements RowMapper<Item> {
        @Override
        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            Item item = new Item();
            item.setItemName(rs.getString("itemName"));

            Category category = new Category();
            category.setCategoryId(rs.getInt("categoryId"));
            category.setLabel(rs.getString("label"));
            item.setCategory(category);

            item.setStartingPrice(rs.getInt("startingPrice"));

            LocalDateTime endingDateTime = rs.getTimestamp("auctionEndingDate").toLocalDateTime();
            item.setAuctionEndingDate(endingDateTime);

            User seller = new User();
            seller.setUsername(rs.getString("username"));
            item.setSeller(seller);


            return item;
        }
    }
}
