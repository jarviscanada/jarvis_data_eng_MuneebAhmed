package ca.jrvs.apps.jdbc.stockquote;

import ca.jrvs.apps.stockquote.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.service.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
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

}
