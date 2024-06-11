package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TraderAccountService {
    private final TraderDao traderDao;
    private final AccountDao accountDao;
    private final SecurityOrderDao securityOrderDao;
    private final PositionDao positionDao;

    @Autowired
    public TraderAccountService(TraderDao traderDao, AccountDao accountDao, SecurityOrderDao securityOrderDao, PositionDao positionDao) {
        this.traderDao = traderDao;
        this.accountDao = accountDao;
        this.securityOrderDao = securityOrderDao;
        this.positionDao = positionDao;
    }

    @Transactional
    public TraderAccountView createTraderAndAccount(Trader trader) {
        if (trader == null || trader.getId() != null ||
                trader.getFirstName() == null || trader.getLastName() == null ||
                trader.getCountry() == null || trader.getDob() == null ||
                trader.getEmail() == null) {
            throw new IllegalArgumentException("Invalid trader information");
        }

        Trader savedTrader = traderDao.save(trader);
        Account account = new Account();
        account.setTraderId(savedTrader.getId());
        account.setAmount(0.0);
        Account savedAccount = accountDao.save(account);

        TraderAccountView traderAccountView = new TraderAccountView();
        traderAccountView.setTrader(savedTrader);
        traderAccountView.setAccount(savedAccount);
        return traderAccountView;
    }

    @Transactional
    public void deleteTraderById(Integer traderId) {
        if (traderId == null) {
            throw new IllegalArgumentException("Trader ID cannot be null");
        }

        Optional<Account> accountOpt = accountDao.findByTraderId(traderId);
        if (!accountOpt.isPresent()) {
            throw new IllegalArgumentException("Trader account not found");
        }

        Account account = accountOpt.get();
        if (account.getAmount() != 0.0) {
            throw new IllegalArgumentException("Account balance must be zero to delete trader");
        }

        if (positionDao.countByAccountId(account.getId()) > 0) {
            throw new IllegalArgumentException("Trader has open positions");
        }

        securityOrderDao.deleteByAccountId(account.getId());
        accountDao.deleteById(account.getId());
        traderDao.deleteById(traderId);
    }

    @Transactional
    public Account deposit(Integer traderId, Double fund) {
        if (traderId == null || fund == null || fund <= 0) {
            throw new IllegalArgumentException("Invalid trader ID or fund amount");
        }

        Account account = accountDao.findByTraderId(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Trader account not found"));
        account.setAmount(account.getAmount() + fund);
        return accountDao.save(account);
    }

    @Transactional
    public Account withdraw(Integer traderId, Double fund) {
        if (traderId == null || fund == null || fund <= 0) {
            throw new IllegalArgumentException("Invalid trader ID or fund amount");
        }

        Account account = accountDao.findByTraderId(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Trader account not found"));
        if (account.getAmount() < fund) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.setAmount(account.getAmount() - fund);
        return accountDao.save(account);
    }
}
