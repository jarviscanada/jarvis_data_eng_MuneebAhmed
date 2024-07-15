package ca.jrvs.apps.trading.dao;


import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.AlphaVantageQuote;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class MarketDataDaoIntTest {

private MarketDataDao dao;

    @Before
    public void init() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(50);

        MarketDataConfig marketDataConfig = new MarketDataConfig();
        marketDataConfig.setHost("https://alpha-vantage.p.rapidapi.com/query");
        marketDataConfig.setToken("73b430a819mshe696c42860583d4p1f52a5jsn50b55e330925");
        marketDataConfig.setRapidApiHost("alpha-vantage.p.rapidapi.com");
        dao = new MarketDataDao(cm, marketDataConfig);
    }

    @Test
    public void findAlphaVantageQuotesByTickers() throws IOException {

        List<AlphaVantageQuote> quoteList = dao.findAllById(Arrays.asList("AAPL", "A"));
        assertEquals(2, quoteList.size());
        assertEquals("AAPL", quoteList.get(0).getTicker());

        for (AlphaVantageQuote quote : quoteList) {
            System.out.println(quote);
        }

        try {
            dao.findAllById(Arrays.asList("AAPL", "INVALID_TICKER"));
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void findByTicker() {
        String ticker = "AAPL";
        AlphaVantageQuote alphaVantageQuote = dao.findById(ticker).get();
        assertEquals(ticker, alphaVantageQuote.getTicker());
        System.out.println(alphaVantageQuote);
    }

}
