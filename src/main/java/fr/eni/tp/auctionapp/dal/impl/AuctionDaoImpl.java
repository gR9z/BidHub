package fr.eni.tp.auctionapp.dal.impl;

import fr.eni.tp.auctionapp.bo.Auction;
import fr.eni.tp.auctionapp.dal.AuctionDao;
import fr.eni.tp.auctionapp.dto.BidHistoryDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class AuctionDaoImpl implements AuctionDao {
    private static final String ORDER_BY_AUCTION_DATE = " ORDER BY auctionDate ";
    private static final String OFFSET_LIMIT = "OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY;";
    private static final String INSERT =
            "INSERT INTO AUCTIONS (userId, itemId, auctionDate, bidAmount)" +
                    "VALUES (:userId, :itemId, :auctionDate, :bidAmount);";
    private static final String SELECT_BY_ID = "SELECT auctionId, userId, itemId, auctionDate, bidAmount FROM AUCTIONS WHERE auctionId = :auctionId;";
    private static final String SELECT_TOP1_BY_ITEM_ID_ORDER_BY_AUCTION_DATE_DESC = "SELECT TOP 1 auctionId, userId, itemId, auctionDate, bidAmount FROM AUCTIONS WHERE itemId = :itemId ORDER BY auctionDate DESC;";
    private static final String SELECT_ALL_BY_ITEM_ID = "SELECT auctionId, userId, itemId, auctionDate, bidAmount FROM AUCTIONS WHERE itemId = :itemId" + ORDER_BY_AUCTION_DATE;
    private static final String SELECT_BY_ITEM_ID_PAGINATED = "SELECT auctionId, userId, itemId, auctionDate, bidAmount FROM AUCTIONS WHERE itemId = :itemId" + ORDER_BY_AUCTION_DATE + OFFSET_LIMIT;
    private static final String SELECT_BY_USER_ID_PAGINATED = "SELECT auctionId, userId, itemId, auctionDate, bidAmount FROM AUCTIONS WHERE userId = :userId" + ORDER_BY_AUCTION_DATE + OFFSET_LIMIT;

    private static final String SELECT_BID_HISTORY_FOR_ITEM =
            "SELECT auctionDate, bidAmount, userId, username, totalCount, lastBidUser\n" +
                    "FROM (\n" +
                    "    SELECT a.*, u.username,\n" +
                    "           (SELECT COUNT(*) FROM auctions WHERE itemId = :itemId) AS totalCount,\n" +
                    "           (SELECT TOP 1 u2.username FROM auctions a2 JOIN users u2 ON a2.userId = u2.userId WHERE a2.itemId = a.itemId ORDER BY auctionDate DESC) AS lastBidUser,\n" +
                    "           ROW_NUMBER() OVER (ORDER BY auctionDate DESC) AS row_num\n" +
                    "    FROM auctions a\n" +
                    "    JOIN users u ON a.userId = u.userId\n" +
                    "    WHERE itemId = :itemId\n" +
                    ") AS subquery\n" +
                    "WHERE row_num BETWEEN :offset + 1 AND :offset + :limit\n" +
                    "ORDER BY auctionDate DESC;";

    private static final String DELETE_BY_AUCTION_ID = "DELETE FROM AUCTIONS WHERE auctionId = :auctionId;";
    private static final String COUNT = "SELECT COUNT(*) AS count FROM AUCTIONS;";
    private static final String COUNT_BY_ITEM_ID = "SELECT COUNT(*) AS count FROM AUCTIONS WHERE itemId = :itemId;";
    private static final String COUNT_BY_ITEM_ID_AND_USER_ID = "SELECT COUNT(*) AS count FROM AUCTIONS WHERE itemId = :itemId AND userId = :userId;";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AuctionDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional // Indique que cette méthode doit être exécutée dans une transaction.
    public void insert(Auction auction) {

        // Création d'un objet MapSqlParameterSource pour passer les paramètres à la requête SQL.
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", auction.getUserId()); // ID de l'utilisateur qui crée l'enchère.
        params.addValue("itemId", auction.getItemId()); // ID de l'item associé à l'enchère.
        params.addValue("auctionDate", auction.getAuctionDate()); // Date et heure de l'enchère.
        params.addValue("bidAmount", auction.getBidAmount()); // Montant de l'enchère.

        // Préparation d'un objet KeyHolder pour récupérer la clé générée lors de l'insertion.
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                INSERT, // Requête SQL à exécuter.
                params, // Paramètres pour la requête.
                keyHolder // Holder pour récupérer la clé générée.
        );

        // Récupération de la clé générée après l'insertion.
        Number key = (Number) keyHolder.getKey();

        // Si une clé a été générée, elle est assignée à l'objet auction.
        if (key != null) {
            auction.setAuctionId(key.intValue());
        }
    }

    @Override
    public Optional<Auction> findByAuctionId(int auctionId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("auctionId", auctionId);

        try {
            Auction auction = namedParameterJdbcTemplate.queryForObject(
                    SELECT_BY_ID,
                    params,
                    new BeanPropertyRowMapper<>(Auction.class)
            );
            return Optional.ofNullable(auction);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Auction> findTopByItemIdOrderByAuctionDateDesc(int itemId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("itemId", itemId);

        try {
            Auction auction = namedParameterJdbcTemplate.queryForObject(SELECT_TOP1_BY_ITEM_ID_ORDER_BY_AUCTION_DATE_DESC, params, new BeanPropertyRowMapper<>(Auction.class));
            return Optional.ofNullable(auction);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Auction> findAllAuctionsByItemId(int itemId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("itemId", itemId);

        try {
            return namedParameterJdbcTemplate.query(SELECT_ALL_BY_ITEM_ID, params, new BeanPropertyRowMapper<>(Auction.class));
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Auction> findAuctionsByItemIdPaginated(int itemId, int page, int size) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("itemId", itemId);

        return PaginationUtils.findPaginated(
                namedParameterJdbcTemplate,
                SELECT_BY_ITEM_ID_PAGINATED,
                params,
                page,
                size,
                new BeanPropertyRowMapper<>(Auction.class)
        );
    }

    @Override
    public List<Auction> findAuctionsByUserIdPaginated(int userId, int page, int size) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);

        return PaginationUtils.findPaginated(namedParameterJdbcTemplate, SELECT_BY_USER_ID_PAGINATED, params, page, size, new BeanPropertyRowMapper<>(Auction.class));
    }

    @Override
    public List<BidHistoryDto> findBidHistoryForItemPaginated(int itemId, int page, int size) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("itemId", itemId);

        return PaginationUtils.findPaginated(
                namedParameterJdbcTemplate,
                SELECT_BID_HISTORY_FOR_ITEM,
                params,
                page,
                size,
                new BidHistoryDtoRowMapper());
    }

    @Override
    public void deleteById(int auctionId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("auctionId", auctionId);

        namedParameterJdbcTemplate.update(DELETE_BY_AUCTION_ID, params);
    }

    @Override
    public int count() {
        return Optional.ofNullable(jdbcTemplate.queryForObject(COUNT, (rs, rowNum) -> rs.getInt("count")))
                .orElse(0);
    }

    @Override
    public int countByItemId(int itemId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("itemId", itemId);

        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(COUNT_BY_ITEM_ID, params, (rs, rowNum) -> rs.getInt("count")))
                .orElse(0);
    }

    @Override
    public int countByItemIdAndUserId(int itemId, int userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("itemId", itemId);
        params.addValue("userId", userId);

        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(COUNT_BY_ITEM_ID_AND_USER_ID, params, (rs, rowNum) -> rs.getInt("count")))
                .orElse(0);
    }

    private static class BidHistoryDtoRowMapper implements RowMapper<BidHistoryDto> {
        @Override
        public BidHistoryDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            BidHistoryDto bidHistory = new BidHistoryDto();
            bidHistory.setAuctionDate(rs.getTimestamp("auctionDate").toLocalDateTime());
            bidHistory.setBidAmount(rs.getInt("bidAmount"));
            bidHistory.setUserId(rs.getInt("userId"));
            bidHistory.setUsername(rs.getString("username"));
            bidHistory.setTotalCount(rs.getInt("totalCount"));
            bidHistory.setLastBidUser(rs.getString("lastBidUser"));

            return bidHistory;
        }
    }
}