package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.dal.CategoryDao;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CategoryDaoImpl implements CategoryDao {

    private static final String INSERT = "INSERT INTO categories (label) VALUES (:label);";
    private static final String SELECT_BY_ID = "SELECT categoryId, label FROM categories WHERE categoryId = :categoryId;";
    private static final String UPDATE = "UPDATE categories SET label = :label WHERE categoryId = :categoryId;";
    private static final String DELETE = "DELETE FROM categories WHERE categoryId = :categoryId;";
    private static final String SELECT_ALL = "SELECT categoryId, label FROM categories ORDER BY label ASC;";
    private static final String SELECT_COUNT_PER_CATEGORY = "SELECT categoryId, COUNT(*) AS count FROM Items GROUP BY categoryId;";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    public CategoryDaoImpl(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            JdbcTemplate jdbcTemplate
    ) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(Category category) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("label", category.getLabel());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(INSERT, params, keyHolder);

        Number key = (Number) keyHolder.getKey();

        if(key != null) {
            category.setCategoryId(key.intValue());
        }
    }

    @Override
    public Optional<Category> getById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("categoryId", id);

        try {
            Category category = namedParameterJdbcTemplate.queryForObject(
                    SELECT_BY_ID,
                    params,
                    (rs, rowNum) -> {
                        Category cat = new Category();
                        cat.setCategoryId(rs.getInt("categoryId"));
                        cat.setLabel(rs.getString("label"));
                        return cat;
                    }
            );
            return Optional.ofNullable(category);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Category category) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("categoryId", category.getCategoryId());
        params.addValue("label", category.getLabel());

        namedParameterJdbcTemplate.update(UPDATE, params);
    }

    @Override
    public void deleteById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("categoryId", id);
        namedParameterJdbcTemplate.update(DELETE, params);
    }

    @Override
    public List<Category> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(Category.class));
    }

    @Override
    public Map<Integer, Integer> countPerCategory() {
        return jdbcTemplate.query(SELECT_COUNT_PER_CATEGORY, rs -> {
            Map<Integer, Integer> itemCountMap = new HashMap<>();
            while (rs.next()) {
                int categoryId = rs.getInt("categoryId");
                int itemCount = rs.getInt("count");
                itemCountMap.put(categoryId, itemCount);
            }
            return itemCountMap;
        });
    }
}
