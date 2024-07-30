package com.example.trading_backend.config;

import com.example.trading_backend.model.Trader;
import com.example.trading_backend.model.Quote;
import com.example.trading_backend.repository.TraderRepository;
import com.example.trading_backend.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Override
    public void run(String... args) throws Exception {
        // Add dummy traders
        traderRepository.save(new Trader(null, "1", "Mike", "Spencer", "1990-01-01", "Canada", "mike@test.com", 0, "<button (click)='deleteTrader'>Delete Trader</button>"));
        traderRepository.save(new Trader(null, "2", "Hellen", "Miller", "1990-01-01", "Austria", "hellen@test.com", 0, "<button (click)='deleteTrader'>Delete Trader</button>"));
        traderRepository.save(new Trader(null, "3", "Jack", "Reed", "1990-01-01", "United Kingdom", "jack@test.com", 0, "<button (click)='deleteTrader'>Delete Trader</button>"));
        traderRepository.save(new Trader(null, "4", "Robert", "Howard", "1990-01-01", "Switzerland", "robert@test.com", 0, "<button (click)='deleteTrader'>Delete Trader</button>"));
        traderRepository.save(new Trader(null, "5", "Dustin", "Wise", "1990-01-01", "Russia", "dustin@test.com", 0, "<button (click)='deleteTrader'>Delete Trader</button>"));
        traderRepository.save(new Trader(null, "6", "Sergio", "Chung", "1990-01-01", "China", "sergio@test.com", 0, "<button (click)='deleteTrader'>Delete Trader</button>"));
        traderRepository.save(new Trader(null, "7", "Magnolia", "Cortez", "1990-01-01", "Malaysia", "magnolia@test.com", 0, "<button (click)='deleteTrader'>Delete Trader</button>"));
        traderRepository.save(new Trader(null, "8", "Jeremy", "Alvarez", "1990-01-01", "Mexico", "jeremy@test.com", 0, "<button (click)='deleteTrader'>Delete Trader</button>"));
        traderRepository.save(new Trader(null, "9", "Valerie", "Lee", "1990-01-01", "Turkey", "valerie@test.com", 0, "<button (click)='deleteTrader'>Delete Trader</button>"));
        traderRepository.save(new Trader(null, "10", "Lydia", "Zeena", "1990-01-01", "Morocco", "hellen@test.com", 0, "<button (click)='deleteTrader'>Delete Trader</button>"));

        // Add dummy quotes
        quoteRepository.save(new Quote(null, "FB", 319.48, 0, 13, 13, 400));
        quoteRepository.save(new Quote(null, "AAPL", 500.23, 0, 18, 18, 100));
        quoteRepository.save(new Quote(null, "MSFT", 100.53, 0, 25, 25, 200));
        quoteRepository.save(new Quote(null, "GOOGL", 500.99, 0, 30, 10, 400));
        quoteRepository.save(new Quote(null, "AMZN", 85.50, 0, 16, 16, 150));
    }
}