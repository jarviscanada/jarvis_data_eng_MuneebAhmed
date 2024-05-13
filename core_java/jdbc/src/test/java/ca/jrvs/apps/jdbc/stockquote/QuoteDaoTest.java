package ca.jrvs.apps.jdbc.stockquote;

import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.Optional;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuoteDaoTest {
    private Connection conn;
    private QuoteDao quoteDao;

    @BeforeAll
    public void setUp() throws Exception {
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/stock_quote", "postgres", "password");
        quoteDao = new QuoteDao(conn);
    }

    @AfterAll
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void testSaveAndFind() {
        Quote quote = new Quote("AAPL", 125.34, 130.00, 123.50, 129.00, 3000000, new Date(), 125.00, 4.00, "+3.20%");
        quoteDao.save(quote);
        Optional<Quote> foundQuote = quoteDao.findById("AAPL");
        Assertions.assertTrue(foundQuote.isPresent());
        Assertions.assertEquals(129.00, foundQuote.get().getPrice());
    }

    @Test
    public void testDelete() {
        quoteDao.deleteById("AAPL");
        Assertions.assertFalse(quoteDao.findById("AAPL").isPresent());
    }

}
