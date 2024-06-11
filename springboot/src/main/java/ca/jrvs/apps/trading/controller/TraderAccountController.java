package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.dao.TraderAccountView;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;

import ca.jrvs.apps.trading.service.TraderAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/trader")
public class TraderAccountController {

    private final TraderAccountService traderAccountService;

    @Autowired
    public TraderAccountController(TraderAccountService traderAccountService) {
        this.traderAccountService = traderAccountService;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public TraderAccountView createTrader(@RequestBody Trader trader) {
        return traderAccountService.createTraderAndAccount(trader);
    }

    @PostMapping("/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    public TraderAccountView createTrader(@PathVariable String firstname,
                                          @PathVariable String lastname,
                                          @PathVariable String dob,
                                          @PathVariable String country,
                                          @PathVariable String email) {
        LocalDate dateOfBirth = LocalDate.parse(dob, DateTimeFormatter.ISO_DATE);
        Trader trader = new Trader();
        trader.setFirstName(firstname);
        trader.setLastName(lastname);
        trader.setDob(java.sql.Date.valueOf(dateOfBirth));
        trader.setCountry(country);
        trader.setEmail(email);
        return traderAccountService.createTraderAndAccount(trader);
    }

    @DeleteMapping("/traderId/{traderId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTrader(@PathVariable Integer traderId) {
        traderAccountService.deleteTraderById(traderId);
    }

    @PutMapping("/deposit/traderId/{traderId}/amount/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public Account depositFund(@PathVariable Integer traderId, @PathVariable Double amount) {
        return traderAccountService.deposit(traderId, amount);
    }

    @PutMapping("/withdraw/traderId/{traderId}/amount/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public Account withdrawFund(@PathVariable Integer traderId, @PathVariable Double amount) {
        return traderAccountService.withdraw(traderId, amount);
    }

}
