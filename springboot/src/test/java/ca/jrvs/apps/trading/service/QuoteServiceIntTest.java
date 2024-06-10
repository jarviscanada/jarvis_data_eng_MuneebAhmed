package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteServiceIntTest {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private QuoteDao quoteDao;

    @BeforeEach
    public void setup() {
        quoteDao.deleteAll();
    }

    @Test
    public void findAlphaVantageQuoteByTicker() {
        String ticker = "AAPL";
        Quote quote = quoteService.saveQuote(ticker);
        assertNotNull(quote);
        assertEquals(ticker, quote.getTicker());
        assertNotNull(quote.getLastPrice());
    }

    @Test
    public void updateMarketData() {
        String ticker = "AAPL";
        quoteService.saveQuote(ticker);
        Quote savedQuote = quoteDao.findById(ticker).orElse(null);
        assertNotNull(savedQuote);

        savedQuote.setLastPrice(1000.0);
        quoteDao.save(savedQuote);

        quoteService.updateMarketData();
        Quote updatedQuote = quoteDao.findById(ticker).orElse(null);
        assertNotNull(updatedQuote);
        assertNotEquals(1000.0, updatedQuote.getLastPrice());
    }

    @Test
    public void saveQuotes() {
        List<String> tickers = Arrays.asList("AAPL", "A");
        List<Quote> quotes = quoteService.saveQuotes(tickers);

        assertEquals(tickers.size(), quotes.size());
        for (Quote quote : quotes) {
            assertNotNull(quote.getTicker());
            assertTrue(tickers.contains(quote.getTicker()));
        }
    }

    @Test
    public void saveQuote() {
        String ticker = "AAPL";
        Quote savedQuote = quoteService.saveQuote(ticker);

        assertNotNull(savedQuote);
        assertEquals(ticker, savedQuote.getTicker());
        assertNotNull(savedQuote.getLastPrice());
    }

    @Test
    public void findAllQuotes() {
        List<String> tickers = Arrays.asList("AAPL", "A");
        quoteService.saveQuotes(tickers);
        List<Quote> quotes = quoteService.findAllQuotes();

        assertEquals(tickers.size(), quotes.size());
        for (Quote quote : quotes) {
            assertNotNull(quote.getTicker());
            assertTrue(tickers.contains(quote.getTicker()));
        }
    }
}
