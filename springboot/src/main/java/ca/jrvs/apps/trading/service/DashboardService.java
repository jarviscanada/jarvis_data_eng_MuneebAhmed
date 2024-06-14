package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.model.view.PortfolioView;
import ca.jrvs.apps.trading.model.view.SecurityRow;
import org.springframework.stereotype.Service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Trader;

import ca.jrvs.apps.trading.model.view.TraderAccountView;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private TraderDao traderDao;
    private AccountDao accountDao;
    private PositionDao positionDao;
    private QuoteDao quoteDao;

    @Autowired
    public DashboardService(TraderDao traderDao, AccountDao accountDao, PositionDao positionDao, QuoteDao quoteDao) {
        this.traderDao = traderDao;
        this.accountDao = accountDao;
        this.positionDao = positionDao;
        this.quoteDao = quoteDao;
    }

    public TraderAccountView getTraderAccount(Integer traderId) {
        if (traderId == null) {
            throw new IllegalArgumentException("Trader ID must not be null");
        }
        Account account = findAccountByTraderId(traderId);
        Trader trader = traderDao.findById(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Trader not found"));

        return new TraderAccountView(trader, account);
    }

    public PortfolioView getProfileViewByTraderId(Integer traderId) {
        if (traderId == null) {
            throw new IllegalArgumentException("Trader ID must not be null");
        }
        Account account = findAccountByTraderId(traderId);
        List<Position> positions = positionDao.findByAccountId(account.getId());
        List<SecurityRow> securityRows = positions.stream().map(position -> {
            String ticker = position.getTicker();
            double marketValue = quoteDao.findById(ticker)
                    .map(quote -> quote.getLastPrice() * position.getPosition())
                    .orElse(0.0);
            return new SecurityRow(ticker, position.getPosition(), marketValue);
        }).collect(Collectors.toList());

        return new PortfolioView(securityRows);
    }

    private Account findAccountByTraderId(Integer traderId) {
        return accountDao.findByTraderId(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid traderId"));
    }
}