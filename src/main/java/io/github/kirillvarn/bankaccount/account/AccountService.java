package io.github.kirillvarn.bankaccount.account;

import java.util.List;
import java.util.Optional;
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

    public Account create(Account account, UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        account.setUser(user);
        return accountRepo.save(account);
    }

    public Account getOne(UUID id, UUID userId) {
        Account acc = accountRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        return acc;
    }

    public List<Account> getAll(UUID userId) {
        List<Account> acc = accountRepo.findAllByUserId(userId);

        return acc;
    }
}
