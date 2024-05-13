package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.Quote;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {
    private Connection c;

    public QuoteDao(Connection connection) {
        this.c = connection;
    }

    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot save a null entity");
        }

        String sql = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (symbol) DO UPDATE SET open = EXCLUDED.open, high = EXCLUDED.high, low = EXCLUDED.low, price = EXCLUDED.price, volume = EXCLUDED.volume, latest_trading_day = EXCLUDED.latest_trading_day, previous_close = EXCLUDED.previous_close, change = EXCLUDED.change, change_percent = EXCLUDED.change_percent";

        try (PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getSymbol());
            stmt.setDouble(2, entity.getOpen());
            stmt.setDouble(3, entity.getHigh());
            stmt.setDouble(4, entity.getLow());
            stmt.setDouble(5, entity.getPrice());
            stmt.setInt(6, entity.getVolume());
            stmt.setDate(7, new java.sql.Date(entity.getLatestTradingDay().getTime()));
            stmt.setDouble(8, entity.getPreviousClose());
            stmt.setDouble(9, entity.getChange());
            stmt.setString(10, entity.getChangePercent());
            stmt.executeUpdate();

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving quote", e);
        }
    }

    @Override
    public Optional<Quote> findById(String symbol) throws IllegalArgumentException {
        if (symbol == null) {
            throw new IllegalArgumentException("Symbol cannot be null");
        }

        String sql = "SELECT * FROM quote WHERE symbol = ?";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, symbol);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Quote(
                        rs.getString("symbol"),
                        rs.getDouble("open"),
                        rs.getDouble("high"),
                        rs.getDouble("low"),
                        rs.getDouble("price"),
                        rs.getInt("volume"),
                        rs.getDate("latest_trading_day"),
                        rs.getDouble("previous_close"),
                        rs.getDouble("change"),
                        rs.getString("change_percent")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding quote by symbol", e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();
        String sql = "SELECT * FROM quote";

        try (Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                quotes.add(new Quote(
                        rs.getString("symbol"),
                        rs.getDouble("open"),
                        rs.getDouble("high"),
                        rs.getDouble("low"),
                        rs.getDouble("price"),
                        rs.getInt("volume"),
                        rs.getDate("latest_trading_day"),
                        rs.getDouble("previous_close"),
                        rs.getDouble("change"),
                        rs.getString("change_percent")
                ));
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

        String sql = "DELETE FROM quote WHERE symbol = ?";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, symbol);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting quote by symbol", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM quote";

        try (Statement stmt = c.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all quotes", e);
        }
    }
}
