package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.bo.Item;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.ItemDao;
import fr.eni.tp.auctionapp.utils.PaginationUtils;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemDaoImpl implements ItemDao {

    private static final String ORDER_BY_RAND = " ORDER BY NEWID() ";
    private static final String OFFSET_LIMIT = "OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY";

    private static final String INSERT = "INSERT INTO ITEMS (itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId) VALUES (:itemName, :description, :auctionStartingDate, :auctionEndingDate, :startingPrice, :sellingPrice, :imageUrl, :userId, :categoryId);";
    private static final String SELECT_BY_ID = "SELECT itemId, itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId FROM ITEMS WHERE itemId = :itemId;";
    private static final String UPDATE_BY_ID = "UPDATE ITEMS SET itemName = :itemName, description = :description, auctionStartingDate = :auctionStartingDate, auctionEndingDate = :auctionEndingDate, startingPrice = :startingPrice, sellingPrice = :sellingPrice, imageUrl = :imageUrl, userId = :userId, categoryId = :categoryId WHERE itemId = :itemId;";
    private static final String DELETE_BY_ID = "DELETE FROM ITEMS WHERE itemId = :itemId;";

    private static final String SELECT_ALL = "SELECT itemId, itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId FROM ITEMS ";

    private static final String SELECT_ALL_PAGINATED = "SELECT itemId, itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId FROM ITEMS " +
            "WHERE auctionEndingDate >= DATEADD(day, -2, GETDATE()) " +
            ORDER_BY_RAND + OFFSET_LIMIT;

    private static final String SELECT_ALL_ITEMS_FROM_USERID_PAGINATED = "SELECT itemId, itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId FROM ITEMS WHERE userId = :userId " + ORDER_BY_RAND + OFFSET_LIMIT;
    private static final String SELECT_BY_CATEGORY_PAGINATED = "SELECT itemId, itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl, userId, categoryId FROM ITEMS WHERE categoryId = :categoryId " + ORDER_BY_RAND + OFFSET_LIMIT;

    private static final String COUNT_ITEM_BY_USER_ID = "SELECT COUNT(*) AS count FROM Items WHERE userId = :userId;";
    private static final String COUNT_BY_CATEGORY_ID = "SELECT COUNT(*) AS count FROM Items WHERE categoryId = :categoryId;";
    private static final String COUNT = "SELECT COUNT(*) AS count FROM Items WHERE auctionEndingDate >= DATEADD(day, -2, GETDATE());";
    private static final String SELECT_LAST_ADDED_ITEMS = "SELECT TOP 6 * FROM ITEMS ORDER BY itemId DESC;";

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
    public Optional<Item> findById(int id) {
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
    public void deleteById(int itemId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("itemId", itemId);
        namedParameterJdbcTemplate.update(DELETE_BY_ID, params);
    }

    @Override
    public List<Item> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new ItemRowMapper());
    }

    @Override
    public List<Item> findAllPaginated(int page, int size) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limit", size);
        params.addValue("offset", (page - 1) * size);

        return PaginationUtils.findPaginated(namedParameterJdbcTemplate, SELECT_ALL_PAGINATED, params, page, size, new ItemRowMapper());
    }

    @Override
    public List<Item> searchItems(String query, List<Integer> categories, int page, int size) {
        StringBuilder sql = new StringBuilder(SELECT_ALL);

        sql.append(" WHERE auctionEndingDate >= DATEADD(day, -2, GETDATE()) ");

        if (query != null && !query.isEmpty()) {
            sql.append("AND ");
            sql.append("(itemName LIKE :query OR description LIKE :query) ");
        }

        if (categories != null && !categories.isEmpty()) {
            sql.append("AND ");
            sql.append("categoryId IN (:categories) ");
        }

        sql.append(ORDER_BY_RAND);
        if (page < 1) page = 1;
        int offset = (page - 1) * size;
        sql.append(" OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("query", "%" + query + "%");
        params.addValue("categories", categories);
        params.addValue("offset", offset);
        params.addValue("limit", size);

        return namedParameterJdbcTemplate.query(sql.toString(), params, new ItemRowMapper());
    }


    @Override
    public List<Item> findByCategoryPaginated(int categoryId, int page, int size) {
        if (page < 1) page = 1;
        int offset = (page - 1) * size;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("categoryId", categoryId);
        params.addValue("limit", size);
        params.addValue("offset", offset);

        return namedParameterJdbcTemplate.query(
                SELECT_BY_CATEGORY_PAGINATED,
                params,
                new ItemRowMapper()
        );
    }

    @Override
    public List<Item> findLastAddedItems() {
        return jdbcTemplate.query(
                SELECT_LAST_ADDED_ITEMS,
                new ItemRowMapper());
    }

    @Override
    public List<Item> findAllByUserIdPaginated(int userId, int page, int size) {

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

    @Override
    public int countByUserId(int userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(COUNT_ITEM_BY_USER_ID, params, Integer.class)).orElse(0);
    }

    @Override
    public int countByCategoryId(int categoryId) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(COUNT_BY_CATEGORY_ID, (rs, rowNum) -> rs.getInt("count")))
                .orElse(0);
    }

    @Override
    public int count() {
        return Optional.ofNullable(jdbcTemplate.queryForObject(COUNT, (rs, rowNum) -> rs.getInt("count")))
                .orElse(0);
    }

    @Override
    public int countFilteredItems(String query, List<Integer> categories) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS count FROM ITEMS WHERE auctionEndingDate >= DATEADD(day, -2, GETDATE())");

        MapSqlParameterSource params = new MapSqlParameterSource();
        if (query != null && !query.isEmpty()) {
            sql.append(" AND (itemName LIKE :query OR description LIKE :query)");
            params.addValue("query", "%" + query + "%");
        }

        if (categories != null && !categories.isEmpty()) {
            if (query != null && !query.isEmpty()) {
                sql.append(" AND ");
            } else {
                sql.append(" AND ");
            }
            sql.append("categoryId IN (:categories)");
            params.addValue("categories", categories);
        }

        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql.toString(), params, (rs, rowNum) -> rs.getInt("count")))
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
