package io.github.kirillvarn.bankaccount.account;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.github.kirillvarn.bankaccount.user.User;
import io.github.kirillvarn.bankaccount.user.UserRepository;

@Service
public class AccountService {
    private AccountRepository accountRepo;
    private UserRepository userRepo;

    public AccountService(AccountRepository accountRepo, UserRepository userRepo) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
    }

    public Account create(Account account, String userName) {
        User user = userRepo.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }

        boolean hasAccounts = accountRepo.existsByUser(user);

        if (!hasAccounts) {
            account.setIsPrimary(true);
        }

        account.setUser(user);
        return accountRepo.save(account);
    }

    public Account getOne(UUID id, String userName) {
        Account acc = accountRepo.findByIdAndUserUsername(id, userName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        return acc;
    }

    public List<Account> getAll(String userName) {
        List<Account> acc = accountRepo.findAllByUserUsername(userName);

        return acc;
    }
}
