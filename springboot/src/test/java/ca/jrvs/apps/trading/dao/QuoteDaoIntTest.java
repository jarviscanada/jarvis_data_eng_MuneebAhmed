package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql(scripts = "classpath:schema.sql")
public class QuoteDaoIntTest {

    @Autowired
    private QuoteDao quoteDao;

    private Quote savedQuote;

    @Before
    public void insertOne() {
        savedQuote = new Quote();
        savedQuote.setAskPrice(10d);
        savedQuote.setAskSize(10);
        savedQuote.setBidPrice(10.2d);
        savedQuote.setBidSize(10);
        savedQuote.setTicker("aapl");
        savedQuote.setLastPrice(10.1d);
        quoteDao.save(savedQuote);
    }

    @After
    public void deleteOne() {
        quoteDao.deleteById(savedQuote.getTicker());
    }

    @Test
    public void testFindById() {
        Quote foundQuote = quoteDao.findById("aapl").get();
        assertEquals(savedQuote.getTicker(), foundQuote.getTicker());
        assertEquals(savedQuote.getAskPrice(), foundQuote.getAskPrice());
        assertEquals(savedQuote.getAskSize(), foundQuote.getAskSize());
        assertEquals(savedQuote.getBidPrice(), foundQuote.getBidPrice());
        assertEquals(savedQuote.getBidSize(), foundQuote.getBidSize());
        assertEquals(savedQuote.getLastPrice(), foundQuote.getLastPrice());
    }

    @Test
    public void testExistsById() {
        assertTrue(quoteDao.existsById("aapl"));
    }
}

