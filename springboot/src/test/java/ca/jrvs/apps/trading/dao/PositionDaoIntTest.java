package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = {TestConfig.class})
@Sql(scripts = "classpath:schema.sql")
public class PositionDaoIntTest {

    @Autowired
    private PositionDao positionDao;

    @Autowired
    private SecurityOrderDao securityOrderDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TraderDao traderDao;

    @Autowired
    private QuoteDao quoteDao;

    private Trader savedTrader;
    private Account savedAccount;
    private Quote savedQuote;
    private SecurityOrder savedOrder;

    @BeforeEach
    public void insertOne() {
        savedTrader = new Trader();
        savedTrader.setFirstName("John");
        savedTrader.setLastName("Doe");
        savedTrader.setCountry("USA");
        savedTrader.setDob(new java.util.Date(2000, 6, 7));
        savedTrader.setEmail("john.doe@example.com");
        savedTrader = traderDao.save(savedTrader);

        savedAccount = new Account();
        savedAccount.setAmount(1000.0);
        savedAccount.setTraderId(savedTrader.getId());
        savedAccount = accountDao.save(savedAccount);

        savedQuote = new Quote();
        savedQuote.setTicker("AAPL");
        savedQuote.setLastPrice(150.0);
        savedQuote.setBidPrice(149.0);
        savedQuote.setBidSize(100);
        savedQuote.setAskPrice(151.0);
        savedQuote.setAskSize(100);
        savedQuote = quoteDao.save(savedQuote);

        savedOrder = new SecurityOrder();
        savedOrder.setAccount(savedAccount);
        savedOrder.setStatus("FILLED");
        savedOrder.setQuote(savedQuote);
        savedOrder.setSize(10);
        savedOrder.setPrice(150.0);
        savedOrder.setNotes("Test order");
        savedOrder = securityOrderDao.save(savedOrder);
    }

    @AfterEach
    public void deleteOne() {
        securityOrderDao.deleteAll();
        quoteDao.deleteAll();
        accountDao.deleteAll();
        traderDao.deleteAll();
    }

    @Test
    public void findAll() {
        List<Position> positions = positionDao.findAll();
        assertFalse(positions.isEmpty());
        Position position = positions.get(0);
        assertEquals(savedAccount.getId(), position.getAccountId().intValue());
        assertEquals(savedQuote.getTicker(), position.getTicker());
        assertEquals(savedOrder.getSize().longValue(), position.getPosition().longValue());
    }

    @Test
    public void count() {
        long count = positionDao.count();
        assertTrue(count > 0);
    }
}
