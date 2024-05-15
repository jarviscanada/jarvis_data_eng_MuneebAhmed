package ca.jrvs.apps.jdbc.stockquote;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.jrvs.apps.stockquote.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.service.PositionService;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import ca.jrvs.apps.stockquote.Position;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PositionService_IntTest {
    static PositionDao positionDao;
    static DatabaseConnectionManager dcm;
    static Connection connection;
    static PositionService positionService;
    static QuoteDao quoteDao;


    @BeforeAll
    static void setup() throws SQLException {
        dcm = new DatabaseConnectionManager("localhost",
                "stock_quote", "postgres", "password");
        connection = dcm.getConnection();
        quoteDao = new QuoteDao(connection);
        positionDao = new PositionDao(connection);
        positionService = new PositionService(positionDao);

        Quote quote = new Quote("AAPL", 150.0, 155.0, 145.0, 152.0, 1000000, Date.valueOf("2024-02-16"), 150.0, 2.0, "1.33%", Timestamp.from(Instant.now()));
        quoteDao.save(quote);
    }

    @BeforeEach
    void init() {
        // Reset the AAPL position for each test
        Position position = new Position("AAPL", 50, 7500.0); // AAPL with 50 shares at $150 each
        positionDao.save(position);
    }

    @Test
    void test_buy_valid_new() {
        Position position = positionService.buy("AAPL", 10, 155.0);
        assertEquals("AAPL", position.getTicker());
        assertEquals(9050.0, position.getValuePaid());  // New purchase added
        assertEquals(60, position.getNumOfShares());  // Existing shares plus new
    }

    @Test
    void test_sell_valid() {
        positionService.sell("AAPL");
        Optional<Position> position = positionDao.findById("AAPL");
        assertFalse(position.isPresent());
    }
}
