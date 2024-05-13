package ca.jrvs.apps.jdbc.stockquote;

import ca.jrvs.apps.stockquote.Position;
import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PositionDaoTest {
    private Connection conn;
    private PositionDao positionDao;
    private QuoteDao quoteDao;

    @BeforeAll
    public void setUp() throws Exception {
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/stock_quote", "postgres", "password");
        positionDao = new PositionDao(conn);
        quoteDao = new QuoteDao(conn);
    }

    @AfterAll
    public void tearDown() throws Exception {
        conn.close();
    }

    @BeforeEach
    public void setupEach() throws Exception {
        // Ensure each test starts with a known database state
        // Delete all positions first to avoid foreign key constraint violations
        positionDao.deleteAll();
        quoteDao.deleteAll();

        // Re-insert the necessary quote for each test
        Quote quote = new Quote("AAPL", 150.00, 155.00, 148.00, 154.00, 1000000, new java.sql.Date(System.currentTimeMillis()), 150.00, 4.00, "+2.67%");
        quoteDao.save(quote);
    }

    @Test
    public void testSaveAndFind() {
        Position position = new Position("AAPL", 100, 12000.00);
        positionDao.save(position);
        Optional<Position> foundPosition = positionDao.findById("AAPL");
        Assertions.assertTrue(foundPosition.isPresent());
        Assertions.assertEquals(100, foundPosition.get().getNumOfShares());
    }

    @Test
    public void testDelete() {
        positionDao.deleteById("AAPL");
        Assertions.assertFalse(positionDao.findById("AAPL").isPresent());
    }
}
