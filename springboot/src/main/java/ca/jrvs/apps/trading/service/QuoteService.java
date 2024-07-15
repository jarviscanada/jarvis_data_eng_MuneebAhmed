package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.AlphaVantageQuote;
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

    public Quote saveQuote(String ticker) {
        AlphaVantageQuote alphaVantageQuote = findAlphaVantageQuoteByTicker(ticker);
        Quote quote = buildQuoteFromAlphaVantageQuote(alphaVantageQuote);
        return quoteDao.save(quote);
    }

    public void updateMarketData() {
        List<Quote> storedQuotes = quoteDao.findAll();
        List<String> tickers = storedQuotes.stream()
                .map(Quote::getTicker)
                .collect(Collectors.toList());
        saveQuotes(tickers);
    }

    public Quote saveQuote(Quote quote) {
        return quoteDao.save(quote);
    }
    public List<Quote> findAllQuotes() {
        return quoteDao.findAll();
    }

    protected static Quote buildQuoteFromAlphaVantageQuote(AlphaVantageQuote alphaVantageQuote) {
        Quote quote = new Quote();
        quote.setTicker(alphaVantageQuote.getTicker());

        quote.setLastPrice(alphaVantageQuote.getLastPrice() != null ? alphaVantageQuote.getLastPrice() : 0.0);
        quote.setBidPrice(alphaVantageQuote.getBidPrice() != null ? alphaVantageQuote.getBidPrice() : 0.0);
        quote.setBidSize(alphaVantageQuote.getBidSize() != null ? alphaVantageQuote.getBidSize() : 0);
        quote.setAskPrice(alphaVantageQuote.getAskPrice() != null ? alphaVantageQuote.getAskPrice() : 0.0);
        quote.setAskSize(alphaVantageQuote.getAskSize() != null ? alphaVantageQuote.getAskSize() : 0);

        return quote;
    }

    public boolean validateTicker(String ticker) {
        try {
            return findAlphaVantageQuoteByTicker(ticker) != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }



}
