package io.github.kirillvarn.bankaccount.account;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    };

    @GetMapping("/api/accounts/{id}/balance")
    public Account getAccount(@PathVariable UUID id) {
        Optional<Account> account = accountService.getBalance(id);

        if (account.isPresent()) {
            return account.get();
        }

        return null;
    }
}