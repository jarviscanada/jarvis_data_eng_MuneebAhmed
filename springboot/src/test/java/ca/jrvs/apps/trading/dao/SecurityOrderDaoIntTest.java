package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ca.jrvs.apps.trading.model.domain.Trader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestConfig.class})
@Sql(scripts = "classpath:schema.sql")
public class SecurityOrderDaoIntTest {
    @Autowired
    private SecurityOrderDao securityOrderDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TraderDao traderDao;

    @Autowired
    private QuoteDao quoteDao;

    private SecurityOrder savedOrder;
    private Account savedAccount;
    private Trader savedTrader;
    private Quote savedQuote;

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
        securityOrderDao.delete(savedOrder);
        quoteDao.deleteById(savedQuote.getTicker());
        accountDao.delete(savedAccount);
        traderDao.delete(savedTrader);
    }

    @Test
    public void findById() {
        Optional<SecurityOrder> foundOrder = securityOrderDao.findById(savedOrder.getId());
        assertTrue(foundOrder.isPresent());
        assertEquals(savedOrder.getId(), foundOrder.get().getId());
    }

    @Test
    public void existsById() {
        assertTrue(securityOrderDao.existsById(savedOrder.getId()));
    }

    @Test
    public void findAll() {
        List<SecurityOrder> orders = securityOrderDao.findAll();
        assertFalse(orders.isEmpty());
    }

    @Test
    public void findAllById() {
        List<SecurityOrder> orders = securityOrderDao.findAllById(Arrays.asList(savedOrder.getId()));
        assertEquals(1, orders.size());
        assertEquals(savedOrder.getPrice(), orders.get(0).getPrice());
    }

    @Test
    public void deleteById() {
        securityOrderDao.deleteById(savedOrder.getId());
        assertFalse(securityOrderDao.existsById(savedOrder.getId()));
    }

    @Test
    public void deleteAll() {
        securityOrderDao.deleteAll();
        assertEquals(0, securityOrderDao.count());
    }

    @Test
    public void count() {
        long count = securityOrderDao.count();
        assertTrue(count > 0);
    }
}
