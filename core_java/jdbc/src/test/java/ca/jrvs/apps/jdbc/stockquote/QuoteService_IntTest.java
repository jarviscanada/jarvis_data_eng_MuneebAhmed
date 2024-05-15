package ca.jrvs.apps.jdbc.stockquote;

import ca.jrvs.apps.stockquote.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.service.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class QuoteService_IntTest {
    private QuoteService quoteService;
    private QuoteDao quoteDao;
    private Connection connection;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "stock_quote", "postgres", "password");
        connection = dcm.getConnection();
        quoteDao = new QuoteDao(connection);
        QuoteHttpHelper httpHelper = new QuoteHttpHelper("6BIUVEQ38AG801L4");
        quoteService = new QuoteService(quoteDao, httpHelper);
    }

    @Test
    void testFetchNewQuote() {
        String ticker = "MSFT";
        Optional<Quote> initialQuote = quoteDao.findById(ticker);
        assertFalse(initialQuote.isPresent());

        quoteService.fetchQuoteDataFromAPI(ticker);
        Optional<Quote> fetchedQuote = quoteDao.findById(ticker);
        assertTrue(fetchedQuote.isPresent());
        assertEquals(ticker, fetchedQuote.get().getSymbol());
    }

//    @Test
//    void testFetchExistingQuote() {
//        Timestamp now = new Timestamp(System.currentTimeMillis());
//        Quote existingQuote = new Quote("IBM", 130.0, 135.0, 125.0, 132.0, 10000, new java.sql.Date(System.currentTimeMillis()), 130.0, 2.0, "+1.54%", now);
//        quoteDao.save(existingQuote);
//
//        Optional<Quote> beforeUpdate = quoteDao.findById("IBM");
//        assertTrue(beforeUpdate.isPresent());
//        Timestamp oldTimestamp = beforeUpdate.get().getTimestamp();
//
//        quoteService.fetchQuoteDataFromAPI("IBM");
//        Optional<Quote> afterUpdate = quoteDao.findById("IBM");
//
//        assertTrue(afterUpdate.isPresent());
//        assertNotEquals(oldTimestamp, afterUpdate.get().getTimestamp());
//    }
//
//    @Test
//    void testFetchInvalidTicker() {
//        String invalidTicker = "INVALID";
//        Exception exception = assertThrows(RuntimeException.class, () -> quoteService.fetchQuoteDataFromAPI(invalidTicker));
//        assertTrue(exception.getMessage().contains("Error fetching quote data"));
//    }
}
