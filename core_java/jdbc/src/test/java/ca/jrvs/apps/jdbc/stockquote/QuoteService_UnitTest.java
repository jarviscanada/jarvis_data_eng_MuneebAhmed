package ca.jrvs.apps.jdbc.stockquote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.service.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class QuoteService_UnitTest {
    @Mock
    private QuoteDao quoteDao;

    @Mock
    private QuoteHttpHelper httpHelper;

    private QuoteService quoteService;

    @BeforeEach
    void setup() {
        quoteService = new QuoteService(quoteDao, httpHelper);
    }

    @Test
    void fetchQuoteDataFromAPI_ValidTicker_ReturnsQuote() throws Exception {
        Timestamp now = new Timestamp(new Date().getTime());
        Quote mockQuote = new Quote("MSFT", 200.0, 205.0, 195.0, 202.0, 15000, new java.sql.Date(System.currentTimeMillis()), 200.0, 2.0, "+1.00%", now);
        when(httpHelper.fetchQuoteInfo("MSFT")).thenReturn(mockQuote);
        when(quoteDao.save(mockQuote)).thenReturn(mockQuote);

        Optional<Quote> result = quoteService.fetchQuoteDataFromAPI("MSFT");
        assertTrue(result.isPresent());
        assertEquals(mockQuote, result.get());
        verify(httpHelper).fetchQuoteInfo("MSFT");
        verify(quoteDao).save(mockQuote);
    }

    @Test
    void fetchQuoteDataFromAPI_InvalidTicker_ReturnsEmpty() throws IOException {
        when(httpHelper.fetchQuoteInfo("INVALID")).thenReturn(null);

        Optional<Quote> result = quoteService.fetchQuoteDataFromAPI("INVALID");
        assertFalse(result.isPresent());
        verify(httpHelper).fetchQuoteInfo("INVALID");
        verify(quoteDao, never()).save(any(Quote.class));
    }
}
