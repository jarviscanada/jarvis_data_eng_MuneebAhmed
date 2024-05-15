package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class StockQuoteController {

    private QuoteService quoteService;
    private PositionService positionService;

    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    /**
     * User interface for our application
     */
    public void initClient() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Type in a stock symbol to buy or sell: ");
            try {
                String stock = br.readLine().trim();
                if (stock.isEmpty()) {
                    System.out.println("Stock symbol cannot be empty. Please try again.");
                    continue;
                }

                Optional<Quote> quoteOpt = quoteService.fetchQuoteDataFromAPI(stock);
                if (!quoteOpt.isPresent()) {
                    System.out.println("No quote found for symbol: " + stock);
                    continue;
                }
                Quote quote = quoteOpt.get();

                Optional<Position> positionOpt = positionService.findById(stock);
                System.out.println("Latest Stock Information: ");
                System.out.println(quote);
                System.out.println("Your Position with this stock: ");
                if (positionOpt.isPresent()) {
                    System.out.println(positionOpt.get());
                } else {
                    System.out.println("You don't own any of this stock.");
                }

                System.out.println("Would you like to buy or sell this stock? (Input 'buy' or 'sell', or press Enter to check another stock): ");
                String userResponse = br.readLine().trim().toLowerCase();

                if (userResponse.isEmpty()) {
                    continue;
                }
                if (userResponse.equals("buy")) {
                    System.out.println("Please enter how many shares to purchase: ");
                    try {
                        int numberShares = Integer.parseInt(br.readLine().trim());
                        if (numberShares <= 0) {
                            throw new NumberFormatException("Number of shares must be positive.");
                        }
                        positionService.buy(quote.getSymbol(), numberShares, quote.getPrice());
                        System.out.printf("You bought %d shares of %s for a total of %.2f \n", numberShares, quote.getSymbol(), quote.getPrice() * numberShares);
                    } catch (NumberFormatException e) {
                        System.out.println("Please input a valid number of shares.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                } else if (userResponse.equals("sell")) {
                    positionService.sell(stock);
                    if (positionOpt.isPresent()) {
                        Position position = positionOpt.get();
                        System.out.printf("You sold %d shares of %s for %.2f \n", position.getNumOfShares(), position.getTicker(), position.getValuePaid());
                    } else {
                        System.out.println("You didn't have any of this stock to sell.");
                    }
                } else {
                    System.out.println("Invalid input. Please type 'buy' or 'sell'.");
                }
            } catch (IOException e) {
                System.err.println("An error occurred: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}