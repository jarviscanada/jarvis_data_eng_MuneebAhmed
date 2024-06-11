package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Trader;

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
public class TraderDaoIntTest {

    @Autowired
    private TraderDao traderDao;

    private Trader savedTrader;

    @BeforeEach
    public void insertOne() {
        savedTrader = new Trader();
        savedTrader.setFirstName("John");
        savedTrader.setLastName("Doe");
        savedTrader.setCountry("USA");
        savedTrader.setDob(new Date(2000, 6, 7));
        savedTrader.setEmail("john.doe@example.com");
        savedTrader = traderDao.save(savedTrader);
    }

    @AfterEach
    public void deleteOne() {
        traderDao.delete(savedTrader);
    }

    @Test
    public void findByID(){
        Trader foundTrader = traderDao.findById(savedTrader.getId()).orElse(null);
        assertNotNull(foundTrader);
        System.out.println(foundTrader);
        assertEquals(savedTrader.getId(), foundTrader.getId());
    }

    @Test
    public void existsById() {
        assertTrue(traderDao.existsById(savedTrader.getId()));
    }

    @Test
    public void findAll() {
        List<Trader> traders = traderDao.findAll();
        assertFalse(traders.isEmpty());
    }

    @Test
    public void findAllById() {
        List<Trader> traders = traderDao.findAllById(Arrays.asList(savedTrader.getId()));
        assertEquals(1, traders.size());
        assertEquals(savedTrader.getCountry(), traders.get(0).getCountry());
    }

    @Test
    public void deleteById() {
        traderDao.deleteById(savedTrader.getId());
        assertFalse(traderDao.existsById(savedTrader.getId()));
    }

    @Test
    public void deleteAll() {
        traderDao.deleteAll();
        assertEquals(0, traderDao.count());
    }

    @Test
    public void count() {
        long count = traderDao.count();
        assertTrue(count > 0);
    }


}
