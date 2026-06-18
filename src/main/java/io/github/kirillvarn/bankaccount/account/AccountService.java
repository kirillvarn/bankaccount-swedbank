package io.github.kirillvarn.bankaccount.account;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class AccountService {
    public Optional<Account> getBalance(UUID id) {
        Account account = new Account(id, "USD", 100);

        return Optional.of(account);
    }
}
