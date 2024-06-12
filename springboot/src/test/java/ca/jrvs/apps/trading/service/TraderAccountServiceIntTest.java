package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestConfig.class})
@Sql(scripts = "classpath:schema.sql")
public class TraderAccountServiceIntTest {

    @Autowired
    private TraderAccountService traderAccountService;

    @Autowired
    private TraderDao traderDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private SecurityOrderDao securityOrderDao;

    @Autowired
    private PositionDao positionDao;

    private Trader savedTrader;
    private Account savedAccount;

    @BeforeEach
    public void setUp() {
        savedTrader = new Trader();
        savedTrader.setFirstName("John");
        savedTrader.setLastName("Doe");
        savedTrader.setCountry("USA");
        savedTrader.setDob(new Date(2000, 6, 7));
        savedTrader.setEmail("john.doe@example.com");
        savedTrader = traderDao.save(savedTrader);

        savedAccount = new Account();
        savedAccount.setTraderId(savedTrader.getId());
        savedAccount.setAmount(0.0);
        savedAccount = accountDao.save(savedAccount);
    }

    @Test
    public void createTraderAndAccount() {
        Trader newTrader = new Trader();
        newTrader.setFirstName("Jane");
        newTrader.setLastName("Doe");
        newTrader.setCountry("Canada");
        newTrader.setDob(new Date(1990, 1, 1));
        newTrader.setEmail("jane.doe@example.com");

        TraderAccountView traderAccountView = traderAccountService.createTraderAndAccount(newTrader);
        assertNotNull(traderAccountView);
        assertNotNull(traderAccountView.getTrader().getId());
        assertEquals(newTrader.getFirstName(), traderAccountView.getTrader().getFirstName());
        assertNotNull(traderAccountView.getAccount().getId());
        assertEquals(0.0, traderAccountView.getAccount().getAmount());
    }

    @Test
    public void deleteTraderById() {
        traderAccountService.deleteTraderById(savedTrader.getId());
        assertFalse(traderDao.findById(savedTrader.getId()).isPresent());
        assertFalse(accountDao.findById(savedAccount.getId()).isPresent());
    }

    @Test
    public void deposit() {
        Double fund = 500.0;
        Account account = traderAccountService.deposit(savedTrader.getId(), fund);
        assertNotNull(account);
        assertEquals(fund, account.getAmount(), 0.001);
    }

    @Test
    public void withdraw() {
        Double fund = 500.0;
        Account depositAccount = traderAccountService.deposit(savedTrader.getId(), fund);
        Double withdrawAmount = 200.0;
        Account withdrawAccount = traderAccountService.withdraw(savedTrader.getId(), withdrawAmount);
        assertNotNull(withdrawAccount);
        assertEquals(fund - withdrawAmount, withdrawAccount.getAmount());
    }

    @Test
    public void testInvalidDeposit() {
        Double fund = -100.0;
        assertThrows(IllegalArgumentException.class, () -> traderAccountService.deposit(savedTrader.getId(), fund));
    }

    @Test
    public void testInvalidWithdraw() {
        Double fund = -100.0;
        assertThrows(IllegalArgumentException.class, () -> traderAccountService.withdraw(savedTrader.getId(), fund));
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        Double fund = 500.0;
        traderAccountService.deposit(savedTrader.getId(), fund);
        Double withdrawAmount = 600.0;
        assertThrows(IllegalArgumentException.class, () -> traderAccountService.withdraw(savedTrader.getId(), withdrawAmount));
    }

}
