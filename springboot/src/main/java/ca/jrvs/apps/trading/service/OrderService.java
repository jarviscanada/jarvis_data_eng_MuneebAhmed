package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final AccountDao accountDao;
    private final SecurityOrderDao securityOrderDao;
    private final QuoteDao quoteDao;
    private final PositionDao positionDao;

    @Autowired
    public OrderService(AccountDao accountDao, SecurityOrderDao securityOrderDao, QuoteDao quoteDao, PositionDao positionDao) {
        this.accountDao = accountDao;
        this.securityOrderDao = securityOrderDao;
        this.quoteDao = quoteDao;
        this.positionDao = positionDao;
    }

    public SecurityOrder executeMarketOrder(MarketOrder orderData) {
        validateMarketOrder(orderData);

        SecurityOrder securityOrder = new SecurityOrder();
        Account account = accountDao.findById(orderData.getTraderId()).orElseThrow(() ->
                new IllegalArgumentException("Invalid trader ID"));

        Quote quote = quoteDao.findById(orderData.getTicker()).orElseThrow(() ->
                new IllegalArgumentException("Invalid ticker"));

        securityOrder.setAccount(account);
        securityOrder.setQuote(quote);
        securityOrder.setSize(orderData.getSize());

        if (orderData.getOption() == MarketOrder.Option.BUY) {
            handleBuyMarketOrder(orderData, securityOrder, account, quote);
        } else if (orderData.getOption() == MarketOrder.Option.SELL) {
            handleSellMarketOrder(orderData, securityOrder, account, quote);
        } else {
            throw new IllegalArgumentException("Invalid order option");
        }

        return securityOrderDao.save(securityOrder);
    }

    private void validateMarketOrder(MarketOrder orderData) {
        if (orderData == null || orderData.getSize() == 0 || orderData.getTicker() == null || orderData.getTicker().isEmpty()) {
            throw new IllegalArgumentException("Invalid market order");
        }
    }

    protected void handleBuyMarketOrder(MarketOrder marketOrder, SecurityOrder securityOrder, Account account, Quote quote) {
        double price = quote.getAskPrice();
        double totalCost = price * marketOrder.getSize();
        System.out.println("Total cost calculated: " + totalCost);

        if (account.getAmount() < totalCost) {
            securityOrder.setStatus("CANCELED");
            securityOrder.setNotes("Insufficient funds");
            System.out.println("Buy order canceled: Insufficient funds");
        } else {
            account.setAmount(account.getAmount() - totalCost);
            accountDao.save(account);
            System.out.println("Account updated: " + account);

            securityOrder.setPrice(price);
            securityOrder.setStatus("FILLED");
            System.out.println("Buy order filled: " + securityOrder);
        }
    }

    protected void handleSellMarketOrder(MarketOrder marketOrder, SecurityOrder securityOrder, Account account, Quote quote) {
        System.out.println("Executing sell order: " + marketOrder);
        Position position = positionDao.findById(new Position.PositionId(account.getId(), marketOrder.getTicker())).orElse(null);
        System.out.println("Position found: " + position);

        if (position == null || position.getPosition() < Math.abs(marketOrder.getSize())) {
            securityOrder.setStatus("CANCELED");
            securityOrder.setNotes("Insufficient position");
        } else {
            double price = quote.getBidPrice();
            double totalProceeds = price * Math.abs(marketOrder.getSize());

            account.setAmount(account.getAmount() + totalProceeds);
            accountDao.save(account);

            securityOrder.setPrice(price);
            securityOrder.setStatus("FILLED");
        }
    }
}