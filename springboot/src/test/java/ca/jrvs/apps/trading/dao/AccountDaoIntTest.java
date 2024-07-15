package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
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
public class AccountDaoIntTest {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TraderDao traderDao;

    private Account savedAccount;
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

        savedAccount = new Account();
        savedAccount.setAmount(1000.0);
        savedAccount.setTraderId(savedTrader.getId());
        savedAccount = accountDao.save(savedAccount);
    }

    @AfterEach
    public void deleteOne() {
        accountDao.delete(savedAccount);
        traderDao.delete(savedTrader);
    }

    @Test
    public void findById() {
        Optional<Account> foundAccount = accountDao.findById(savedAccount.getId());
        assertTrue(foundAccount.isPresent());
        assertEquals(savedAccount.getId(), foundAccount.get().getId());
    }

    @Test
    public void existsById() {
        assertTrue(accountDao.existsById(savedAccount.getId()));
    }

    @Test
    public void findAll() {
        List<Account> accounts = accountDao.findAll();
        assertFalse(accounts.isEmpty());
    }

    @Test
    public void findAllById() {
        List<Account> accounts = accountDao.findAllById(Arrays.asList(savedAccount.getId()));
        assertEquals(1, accounts.size());
        assertEquals(savedAccount.getAmount(), accounts.get(0).getAmount());
    }

    @Test
    public void deleteById() {
        accountDao.deleteById(savedAccount.getId());
        assertFalse(accountDao.existsById(savedAccount.getId()));
    }

    @Test
    public void deleteAll() {
        accountDao.deleteAll();
        assertEquals(0, accountDao.count());
    }

    @Test
    public void count() {
        long count = accountDao.count();
        assertTrue(count > 0);
    }

}
