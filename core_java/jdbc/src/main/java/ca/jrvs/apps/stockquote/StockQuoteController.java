package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class StockQuoteController {
    private static final Logger logger = LoggerFactory.getLogger(StockQuoteController.class);
    private QuoteService quoteService;
    private PositionService positionService;

    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    public void initClient() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                displayMenu();
                String choice = br.readLine().trim();
                switch (choice) {
                    case "1":
                        viewStockQuote(br);
                        break;
                    case "2":
                        buyStock(br);
                        break;
                    case "3":
                        sellStock(br);
                        break;
                    case "4":
                        viewPortfolio();
                        break;
                    case "5":
                        logger.info("Exiting the application. Goodbye!");
                        return;
                    default:
                        logger.warn("Invalid choice. Please select an option from the menu.");
                }
            } catch (IOException e) {
                logger.error("An error occurred while reading user input.", e);
            }
        }
    }

    private void displayMenu() {
        logger.info("\nStock Quote App Menu:");
        System.out.println("1. View stock quote");
        System.out.println("2. Buy stock");
        System.out.println("3. Sell stock");
        System.out.println("4. View portfolio");
        System.out.println("5. Exit");
        System.out.print("Please select an option: ");
    }

    private void viewStockQuote(BufferedReader br) throws IOException {
        System.out.print("Enter stock symbol to view: ");
        String stock = br.readLine().trim();
        if (stock.isEmpty()) {
            logger.warn("Stock symbol cannot be empty. Please try again.");
            return;
        }
        Optional<Quote> quoteOpt = quoteService.fetchQuoteDataFromAPI(stock);
        if (quoteOpt.isPresent()) {
            Quote quote = quoteOpt.get();
            logger.info("Latest Stock Information: {}", quote);
            System.out.println("Latest Stock Information: ");
            System.out.println(quote);
        } else {
            logger.warn("No quote found for symbol: {}", stock);
            System.out.println("No quote found for symbol: " + stock);
        }
    }

    private void buyStock(BufferedReader br) throws IOException {
        System.out.print("Enter stock symbol to buy: ");
        String stock = br.readLine().trim();
        if (stock.isEmpty()) {
            logger.warn("Stock symbol cannot be empty. Please try again.");
            return;
        }
        Optional<Quote> quoteOpt = quoteService.fetchQuoteDataFromAPI(stock);
        if (!quoteOpt.isPresent()) {
            logger.warn("No quote found for symbol: {}", stock);
            System.out.println("No quote found for symbol: " + stock);
            return;
        }
        Quote quote = quoteOpt.get();
        logger.info("Latest Stock Information: {}", quote);
        System.out.println("Latest Stock Information: ");
        System.out.println(quote);

        System.out.print("Please enter how many shares to purchase: ");
        try {
            int numberShares = Integer.parseInt(br.readLine().trim());
            if (numberShares <= 0) {
                throw new NumberFormatException("Number of shares must be positive.");
            }
            positionService.buy(quote.getSymbol(), numberShares, quote.getPrice());
            logger.info("Bought {} shares of {} for a total of {}", numberShares, quote.getSymbol(), quote.getPrice() * numberShares);
            System.out.printf("You bought %d shares of %s for a total of %.2f \n", numberShares, quote.getSymbol(), quote.getPrice() * numberShares);
        } catch (NumberFormatException e) {
            logger.warn("Invalid number of shares.", e);
            System.out.println("Please input a valid number of shares.");
        } catch (IllegalArgumentException e) {
            logger.warn("Error buying shares: {}", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private void sellStock(BufferedReader br) throws IOException {
        System.out.print("Enter stock symbol to sell: ");
        String stock = br.readLine().trim();
        if (stock.isEmpty()) {
            logger.warn("Stock symbol cannot be empty. Please try again.");
            System.out.println("Stock symbol cannot be empty. Please try again.");
            return;
        }
        Optional<Position> positionOpt = positionService.findById(stock);
        if (positionOpt.isPresent()) {
            positionService.sell(stock);
            Position position = positionOpt.get();
            logger.info("Sold {} shares of {} for {}", position.getNumOfShares(), position.getTicker(), position.getValuePaid());
            System.out.printf("You sold %d shares of %s for %.2f \n", position.getNumOfShares(), position.getTicker(), position.getValuePaid());
        } else {
            logger.warn("No position found for ticker: {}", stock);
            System.out.println("You don't own any of this stock.");
        }
    }

    private void viewPortfolio() {
        logger.info("Retrieving portfolio");
        System.out.println("Your Portfolio: ");
        positionService.getPortfolio().forEach(System.out::println);
    }
}