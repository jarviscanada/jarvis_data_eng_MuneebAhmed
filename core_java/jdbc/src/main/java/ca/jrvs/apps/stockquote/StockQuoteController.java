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
                        System.out.println("Exiting the application. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select an option from the menu.");
                }
            } catch (IOException e) {
                System.err.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void displayMenu() {
        System.out.println("\nStock Quote App Menu:");
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
            System.out.println("Stock symbol cannot be empty. Please try again.");
            return;
        }
        Optional<Quote> quoteOpt = quoteService.fetchQuoteDataFromAPI(stock);
        if (quoteOpt.isPresent()) {
            Quote quote = quoteOpt.get();
            System.out.println("Latest Stock Information: ");
            System.out.println(quote);
        } else {
            System.out.println("No quote found for symbol: " + stock);
        }
    }

    private void buyStock(BufferedReader br) throws IOException {
        System.out.print("Enter stock symbol to buy: ");
        String stock = br.readLine().trim();
        if (stock.isEmpty()) {
            System.out.println("Stock symbol cannot be empty. Please try again.");
            return;
        }
        Optional<Quote> quoteOpt = quoteService.fetchQuoteDataFromAPI(stock);
        if (!quoteOpt.isPresent()) {
            System.out.println("No quote found for symbol: " + stock);
            return;
        }
        Quote quote = quoteOpt.get();
        System.out.println("Latest Stock Information: ");
        System.out.println(quote);

        System.out.print("Please enter how many shares to purchase: ");
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
    }

    private void sellStock(BufferedReader br) throws IOException {
        System.out.print("Enter stock symbol to sell: ");
        String stock = br.readLine().trim();
        if (stock.isEmpty()) {
            System.out.println("Stock symbol cannot be empty. Please try again.");
            return;
        }
        Optional<Position> positionOpt = positionService.findById(stock);
        if (positionOpt.isPresent()) {
            positionService.sell(stock);
            Position position = positionOpt.get();
            System.out.printf("You sold %d shares of %s for %.2f \n", position.getNumOfShares(), position.getTicker(), position.getValuePaid());
        } else {
            System.out.println("You don't own any of this stock.");
        }
    }

    private void viewPortfolio() {
        System.out.println("Your Portfolio: ");
        positionService.getPortfolio().forEach(System.out::println);
    }
}