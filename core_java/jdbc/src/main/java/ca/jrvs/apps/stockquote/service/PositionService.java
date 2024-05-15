package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;

import java.util.List;
import java.util.Optional;

public class PositionService {
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
        Optional<Position> existingPosition = dao.findById(ticker);
        Position position;
        if (existingPosition.isPresent()) {
            position = existingPosition.get();
            position.setNumOfShares(position.getNumOfShares() + numberOfShares);
            position.setValuePaid(position.getValuePaid() + (numberOfShares * price));
        } else {
            position = new Position(ticker, numberOfShares, numberOfShares * price);
        }
        dao.save(position);
        return position;
    }

    /**
     * Sells all shares of the given ticker symbol and deletes the position from the database.
     * @param ticker Symbol of the stock to sell
     */
    public void sell(String ticker) {
        Optional<Position> existingPosition = dao.findById(ticker);
        existingPosition.ifPresent(position -> dao.deleteById(ticker));
    }

    public Optional<Position> findById(String ticker) {
        return dao.findById(ticker);
    }

    public List<Position> getPortfolio() {
        return dao.findAll();
    }
}
