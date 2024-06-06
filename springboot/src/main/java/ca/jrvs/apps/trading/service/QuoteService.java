package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {
    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

    private final MarketDataDao marketDataDao;

    @Autowired
    public QuoteService(MarketDataDao marketDataDao) {
        this.marketDataDao = marketDataDao;
    }

    public IexQuote findIexQuoteByTicker(String ticker) {
        logger.debug("Finding IexQuote for ticker: {}", ticker);
        if (ticker == null || ticker.isEmpty()) {
            throw new IllegalArgumentException("Ticker cannot be null or empty");
        }
        return marketDataDao.findById(ticker).orElseThrow(() ->
                new IllegalArgumentException("Invalid ticker: " + ticker));
    }
}
