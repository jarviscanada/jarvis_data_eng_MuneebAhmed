package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class PositionService {
    private static final Logger logger = LoggerFactory.getLogger(PositionService.class);

    private PositionDao dao;


    public PositionService(PositionDao dao) {
        this.dao = dao;
    }

    /**
     * Processes a buy order and updates the database accordingly.
     * @param ticker Symbol of the stock to buy
     * @param numberOfShares Number of shares to buy
     * @param price Price at which the shares were bought
     * @return The updated or newly created position
     */
    public Position buy(String ticker, int numberOfShares, double price) {
        logger.info("Buying {} shares of {} at price {}", numberOfShares, ticker, price);
        Optional<Position> existingPosition = dao.findById(ticker);
        Position position;
        if (existingPosition.isPresent()) {
            position = existingPosition.get();
            position.setNumOfShares(position.getNumOfShares() + numberOfShares);
            position.setValuePaid(position.getValuePaid() + (numberOfShares * price));
            logger.info("Updated existing position: {}", position);
        } else {
            position = new Position(ticker, numberOfShares, numberOfShares * price);
            logger.info("Created new position: {}", position);
        }
        dao.save(position);
        return position;
    }

    /**
     * Sells all shares of the given ticker symbol and deletes the position from the database.
     * @param ticker Symbol of the stock to sell
     */
    public void sell(String ticker) {
        logger.info("Selling all shares of {}", ticker);
        Optional<Position> existingPosition = dao.findById(ticker);
        existingPosition.ifPresent(position -> {
            dao.deleteById(ticker);
            logger.info("Deleted position: {}", position);
        });
    }

    public Optional<Position> findById(String ticker) {
        logger.info("Finding position by ticker: {}", ticker);
        return dao.findById(ticker);
    }

    public List<Position> getPortfolio() {
        logger.info("Retrieving all positions");
        return dao.findAll();
    }
}
