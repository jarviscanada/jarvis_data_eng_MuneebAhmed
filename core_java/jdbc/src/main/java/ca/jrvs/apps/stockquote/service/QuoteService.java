package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class QuoteService {
    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;

    public QuoteService(QuoteDao dao, QuoteHttpHelper httpHelper) {
        this.dao = dao;
        this.httpHelper = httpHelper;
    }

    public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        logger.info("Fetching quote data for ticker: {}", ticker);
        try {
            Quote quote = httpHelper.fetchQuoteInfo(ticker);
            if (quote != null) {
                dao.save(quote);
                logger.info("Fetched and saved quote data: {}", quote);
                return Optional.of(quote);
            }else{
                logger.warn("No quote data found for ticker: {}", ticker);
            }
        } catch (Exception e) {
            logger.error("Error fetching quote data for ticker: {}", ticker, e);
        }
        return Optional.empty();
    }
}
