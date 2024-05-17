package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {
    private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
    private Connection c;
    private static final String INSERT = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, COALESCE(?, CURRENT_TIMESTAMP)) ON CONFLICT (symbol) DO UPDATE SET open = EXCLUDED.open, high = EXCLUDED.high, low = EXCLUDED.low, price = EXCLUDED.price, volume = EXCLUDED.volume, latest_trading_day = EXCLUDED.latest_trading_day, previous_close = EXCLUDED.previous_close, change = EXCLUDED.change, change_percent = EXCLUDED.change_percent";
    private static final String SELECT_BY_ID = "SELECT * FROM quote WHERE symbol = ?";
    private static final String SELECT_ALL = "SELECT * FROM quote";
    private static final String DELETE = "DELETE FROM quote WHERE symbol = ?";
    private static final String DELETE_ALL = "DELETE FROM quote";

    public QuoteDao(Connection connection) {
        this.c = connection;
    }

    @Override
    public Quote save(Quote quote) throws IllegalArgumentException {
        if (quote == null) {
            logger.error("Cannot save a null quote");
            throw new IllegalArgumentException("Cannot save a null quote");
        }

        try (PreparedStatement statement = c.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, quote.getSymbol());
            statement.setDouble(2, quote.getOpen());
            statement.setDouble(3, quote.getHigh());
            statement.setDouble(4, quote.getLow());
            statement.setDouble(5, quote.getPrice());
            statement.setInt(6, quote.getVolume());
            statement.setObject(7, quote.getLatestTradingDay() != null ? new java.sql.Date(quote.getLatestTradingDay().getTime()) : null);
            statement.setDouble(8, quote.getPreviousClose());
            statement.setDouble(9, quote.getChange());
            statement.setString(10, quote.getChangePercent());
            statement.setTimestamp(11, quote.getTimestamp());
            statement.execute();
            logger.info("Quote saved: {}", quote);
            return quote;
        } catch (SQLException e) {
            logger.error("Error saving quote: {}", quote, e);
            throw new RuntimeException("Error saving quote", e);
        }
    }

    @Override
    public Optional<Quote> findById(String symbol) throws IllegalArgumentException {
        if (symbol == null) {
            logger.error("Symbol cannot be null");
            throw new IllegalArgumentException("Symbol cannot be null");
        }

        try (PreparedStatement statement = c.prepareStatement(SELECT_BY_ID)) {
            statement.setString(1, symbol);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Quote quote = mapToQuote(rs);
                logger.info("Found quote: {}", quote);
                return Optional.of(quote);
            }
        } catch (SQLException e) {
            logger.error("Error finding quote by symbol: {}", symbol, e);
            throw new RuntimeException("Error finding quote by symbol", e);
        }
        logger.warn("No quote found for symbol: {}", symbol);
        return Optional.empty();
    }

    @Override
    public List<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();
        try (Statement statement = c.createStatement();
             ResultSet rs = statement.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                Quote quote = mapToQuote(rs);
                quotes.add(quote);
                logger.info("Found quote: {}", quote);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all quotes", e);
            throw new RuntimeException("Error retrieving all quotes", e);
        }
        return quotes;
    }

    @Override
    public void deleteById(String symbol) throws IllegalArgumentException {
        if (symbol == null) {
            logger.error("Symbol cannot be null");
            throw new IllegalArgumentException("Symbol cannot be null");
        }

        try (PreparedStatement statement = c.prepareStatement(DELETE)) {
            statement.setString(1, symbol);
            statement.execute();
            logger.info("Deleted quote with symbol: {}", symbol);
        } catch (SQLException e) {
            logger.error("Error deleting quote by symbol: {}", symbol, e);
            throw new RuntimeException("Error deleting quote by symbol", e);
        }
    }

    @Override
    public void deleteAll() {
        try (Statement statement = c.createStatement()) {
            statement.execute(DELETE_ALL);
            logger.info("Deleted all quotes");
        } catch (SQLException e) {
            logger.error("Error deleting all quotes", e);
            throw new RuntimeException("Error deleting all quotes", e);
        }
    }

    private Quote mapToQuote(ResultSet rs) throws SQLException {
        return new Quote(
                rs.getString("symbol"),
                rs.getDouble("open"),
                rs.getDouble("high"),
                rs.getDouble("low"),
                rs.getDouble("price"),
                rs.getInt("volume"),
                rs.getDate("latest_trading_day"),
                rs.getDouble("previous_close"),
                rs.getDouble("change"),
                rs.getString("change_percent"),
                rs.getTimestamp("timestamp")
        );
    }
}
