package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.AlphaVantageQuote;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteService {
    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

    private final MarketDataDao marketDataDao;
    private final QuoteDao quoteDao;

    @Autowired
    public QuoteService(MarketDataDao marketDataDao, QuoteDao quoteDao) {
        this.marketDataDao = marketDataDao;
        this.quoteDao = quoteDao;
    }

    public AlphaVantageQuote findAlphaVantageQuoteByTicker(String ticker) {
        logger.debug("Finding AlphaVantageQuote for ticker: {}", ticker);
        if (ticker == null || ticker.isEmpty()) {
            throw new IllegalArgumentException("Ticker cannot be null or empty");
        }
        return marketDataDao.findById(ticker).orElseThrow(() ->
                new IllegalArgumentException("Invalid ticker: " + ticker));
    }

    public List<Quote> saveQuotes(List<String> tickers) {
        List<Quote> quotes = new ArrayList<>(tickers.size());
        for (String ticker : tickers) {
            Quote quoteReceived = saveQuote(ticker);
            quotes.add(quoteReceived);
        }
        return quotes;
    }

    private Quote saveQuote(String ticker) {
        AlphaVantageQuote alphaVantageQuote = findAlphaVantageQuoteByTicker(ticker);
        Quote quote = new Quote();
        quote.setTicker(alphaVantageQuote.getTicker());
        quote.setLastPrice(alphaVantageQuote.getLastPrice());
        quote.setBidPrice(alphaVantageQuote.getBidPrice());
        quote.setBidSize(alphaVantageQuote.getBidSize());
        quote.setAskPrice(alphaVantageQuote.getAskPrice());
        quote.setAskSize(alphaVantageQuote.getAskSize());
        return quoteDao.save(quote);
    }

    public void updateMarketData() {
        List<Quote> storedQuotes = quoteDao.findAll();
        List<String> tickers = storedQuotes.stream()
                .map(Quote::getTicker)
                .collect(Collectors.toList());
        saveQuotes(tickers);
    }


}
