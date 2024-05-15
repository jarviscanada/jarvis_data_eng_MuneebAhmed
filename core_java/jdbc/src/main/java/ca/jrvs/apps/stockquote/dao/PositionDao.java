package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.Position;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String> {
    private Connection c;
    private static final String INSERT = "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE position SET number_of_shares = ?, value_paid = ? WHERE symbol = ?";
    private static final String DELETE = "DELETE FROM position WHERE symbol = ?";
    private static final String DELETE_ALL = "DELETE FROM position";
    private static final String SELECT_BY_ID = "SELECT * FROM position WHERE symbol = ?";
    private static final String SELECT_ALL = "SELECT * FROM position";
    public PositionDao(Connection connection) {
        this.c = connection;
    }

    @Override
    public Position save(Position position) throws IllegalArgumentException {
        if (position == null) {
            throw new IllegalArgumentException("Cannot save a null entity");
        }

        Optional<Position> existingPosition = findById(position.getTicker());
        String sql = existingPosition.isPresent() ? UPDATE : INSERT;
        try (PreparedStatement statement = this.c.prepareStatement(sql)) {
            if (existingPosition.isPresent()) {
                statement.setInt(1, position.getNumOfShares());
                statement.setDouble(2, position.getValuePaid());
                statement.setString(3, position.getTicker());
            } else {
                statement.setString(1, position.getTicker());
                statement.setInt(2, position.getNumOfShares());
                statement.setDouble(3, position.getValuePaid());
            }
            statement.executeUpdate();
            return findById(position.getTicker()).get();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving position", e);
        }
    }

    @Override
    public Optional<Position> findById(String ticker) throws IllegalArgumentException {
        if (ticker == null) {
            throw new IllegalArgumentException("Ticker cannot be null");
        }

        try (PreparedStatement statement = this.c.prepareStatement(SELECT_BY_ID)) {
            statement.setString(1, ticker);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToPosition(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding position by ticker", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Position> findAll() {
        List<Position> positions = new ArrayList<>();
        try (PreparedStatement statement = this.c.prepareStatement(SELECT_ALL)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                positions.add(mapToPosition(rs));
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

        try (PreparedStatement statement = this.c.prepareStatement(DELETE)) {
            statement.setString(1, ticker);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting position by ticker", e);
        }
    }

    @Override
    public void deleteAll() {
        try (PreparedStatement statement = this.c.prepareStatement(DELETE_ALL)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all positions", e);
        }
    }

    private Position mapToPosition(ResultSet rs) throws SQLException {
        Position position = new Position();
        position.setTicker(rs.getString("symbol"));
        position.setNumOfShares(rs.getInt("number_of_shares"));
        position.setValuePaid(rs.getDouble("value_paid"));
        return position;
    }
}
