package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private AccountDao accountRepo;

    @Autowired
    public AccountService(AccountDao accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Transactional
    public void deleteAccountByTraderId(Integer traderId) {
        Account account = accountRepo.getAccountByTraderId(traderId);
        if (account.getAmount() != 0) {
            throw new IllegalArgumentException("Balance not 0");
        }
        accountRepo.deleteById(account.getId());
    }
}
