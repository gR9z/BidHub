package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.ItemDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {

    private static final String INSERT = "INSERT INTO ITEMS (itemName, description, categoryId, startingPrice, auctionStartingDate, auctionEndingDate) VALUES (:itemName, :description, :startingPrice, :auctionStartingDate, :auctionEndingDate)";
    private static final String SELECT_BY_ID = "SELECT it.itemId, it.description, cat.label, it.sellingPrice, it.startingPrice, it.auctionStartingDate, it.auctionEndingDate, wth.street, wth.zipCode, wth.city, us.username FROM ITEMS AS it" +
            "   INNER JOIN CATEGORIES AS cat ON it.categoryId = cat.categoryId" +
            "   INNER JOIN WITHDRAWALS AS wth ON it.itemId = wth.itemId" +
            "   INNER JOIN USERS AS us ON it.userId = us.userId" +
            "   WHERE it.itemId = :id";
    private static final String SELECT_ALL = "SELECT it.itemName, it.startingPrice, it.auctionEndingDate, us.username, cat.categoryId, cat.label FROM ITEMS AS it" +
            "   INNER JOIN CATEGORIES AS cat ON it.categoryId = cat.categoryId" +
            "   INNER JOIN USERS AS us ON it.userId = us.userId" +
            "   SELECT SCOPE_IDENTITY()";


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
        namedParameters.addValue("auctionStartingDate", item.getAuctionStartingDate());
        namedParameters.addValue("auctionEndingDate", item.getAuctionEndingDate());


        namedParameterJdbcTemplate.update(
                INSERT,
                namedParameters
        );

    }

    @Override
    public Item read(long id) {
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

    }

    @Override
    public void delete(Item item) {

    }

    @Override
    public List<Item> findAll() {
        List<Item> items = jdbcTemplate.query(
                SELECT_ALL,
                new ItemRowMapper()
        );

        return items;
    }




    private class ItemRowMapper implements RowMapper<Item> {
        @Override
        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            Item item = new Item();
            item.setItemId(rs.getInt("itemId"));
            item.setItemName(rs.getString("itemName"));

            Category category = new Category();
            category.setCategoryId(rs.getString("categoryId"));
            category.setLabel(rs.getString("label"));

            item.setSellingPrice(rs.getInt("sellingPrice"));
            item.setStartingPrice(rs.getInt("startingPrice"));
            item.setAuctionStartingDate(rs.getDate("auctionStartingDate"));
            item.setAuctionEndingDate(rs.getDate("auctionEndingDate"));

            Withdrawal withdrawal = new withdrawal();
            withdrawal.setStreet(rs.getString("street"));
            withdrawal.setZipCode(rs.getInt("zipCode"));
            withdrawal.setCity(rs.getString("city"));

            User seller = new User();
            seller.setUsername(rs.getString("username"));

            item.setCategory(category);
            item.setWithdrawal(withdrawal);
            item.setUser(seller);


            return item;
        }
    }
}
