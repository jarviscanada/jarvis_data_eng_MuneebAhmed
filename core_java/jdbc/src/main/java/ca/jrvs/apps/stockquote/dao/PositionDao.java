package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.Position;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String> {
    private Connection c;
    public PositionDao(Connection connection) {
        this.c = connection;
    }

    @Override
    public Position save(Position entity) throws IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot save a null entity");
        }

        String sql = "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?, ?, ?) ON CONFLICT (symbol) DO UPDATE SET number_of_shares = EXCLUDED.number_of_shares, value_paid = EXCLUDED.value_paid";

        try (PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getTicker());
            stmt.setInt(2, entity.getNumOfShares());
            stmt.setDouble(3, entity.getValuePaid());
            stmt.executeUpdate();

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving position", e);
        }
    }

    @Override
    public Optional<Position> findById(String ticker) throws IllegalArgumentException {
        if (ticker == null) {
            throw new IllegalArgumentException("Ticker cannot be null");
        }

        String sql = "SELECT * FROM position WHERE symbol = ?";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, ticker);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Position position = new Position();
                position.setTicker(rs.getString("symbol"));
                position.setNumOfShares(rs.getInt("number_of_shares"));
                position.setValuePaid(rs.getDouble("value_paid"));
                return Optional.of(position);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding position by ticker", e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Position> findAll() {
        List<Position> positions = new ArrayList<>();
        String sql = "SELECT * FROM position";

        try (Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Position position = new Position();
                position.setTicker(rs.getString("symbol"));
                position.setNumOfShares(rs.getInt("number_of_shares"));
                position.setValuePaid(rs.getDouble("value_paid"));
                positions.add(position);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all positions", e);
        }
        return positions;
    }

    @Override
    public void deleteById(String ticker) throws IllegalArgumentException {
        if (ticker == null) {
            throw new IllegalArgumentException("Ticker cannot be null");
        }

        String sql = "DELETE FROM position WHERE symbol = ?";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, ticker);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting position by ticker", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM position";

        try (Statement stmt = c.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all positions", e);
        }
    }
}
