package fr.eni.tp.auctionapp.utils;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;

public class PaginationUtils {

    public static <T> List<T> findPaginated(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String query, MapSqlParameterSource params, int page, int size, RowMapper<T> rowMapper) {
        if (page < 1) page = 1;
        int offset = (page - 1) * size;

        params.addValue("limit", size);
        params.addValue("offset", offset);

        try {
            return namedParameterJdbcTemplate.query(
                    query,
                    params,
                    rowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }
}
