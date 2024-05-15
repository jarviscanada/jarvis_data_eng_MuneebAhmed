package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.Quote;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {
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
            return quote;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving quote", e);
        }
    }

    @Override
    public Optional<Quote> findById(String symbol) throws IllegalArgumentException {
        if (symbol == null) {
            throw new IllegalArgumentException("Symbol cannot be null");
        }

        try (PreparedStatement statement = c.prepareStatement(SELECT_BY_ID)) {
            statement.setString(1, symbol);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToQuote(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding quote by symbol", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();
        try (Statement statement = c.createStatement();
             ResultSet rs = statement.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                quotes.add(mapToQuote(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all quotes", e);
        }
        return quotes;
    }

    @Override
    public void deleteById(String symbol) throws IllegalArgumentException {
        if (symbol == null) {
            throw new IllegalArgumentException("Symbol cannot be null");
        }

        try (PreparedStatement statement = c.prepareStatement(DELETE)) {
            statement.setString(1, symbol);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting quote by symbol", e);
        }
    }

    @Override
    public void deleteAll() {
        try (Statement statement = c.createStatement()) {
            statement.execute(DELETE_ALL);
        } catch (SQLException e) {
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
