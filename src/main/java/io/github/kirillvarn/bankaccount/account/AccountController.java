package io.github.kirillvarn.bankaccount.account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import io.github.kirillvarn.bankaccount.Mapper;
import io.github.kirillvarn.bankaccount.transaction.Transaction;
import io.github.kirillvarn.bankaccount.transaction.TransactionDto;
import io.github.kirillvarn.bankaccount.transaction.TransactionService;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private AccountService accountService;
    private TransactionService transactionService;
    private Mapper<Account, AccountDto> mapper;
    private Mapper<Transaction, TransactionDto> transactionMapper;

    @Autowired
    public AccountController(Mapper<Transaction, TransactionDto> transactionMapper, TransactionService transactionService, AccountService accountService, Mapper<Account, AccountDto> mapper) {
        this.accountService = accountService;
        this.transactionMapper = transactionMapper;
        this.transactionService = transactionService;
        this.mapper = mapper;
    };

    @PostMapping
    public AccountDto create(@AuthenticationPrincipal Jwt jwt, @RequestBody AccountDto accountDto) {
        Account acc = mapper.mapFrom(accountDto);
        UUID userId = UUID.fromString(jwt.getClaim("user_id"));
        Account saved = accountService.create(acc, userId);
        return mapper.mapTo(saved);
    }

    @GetMapping("/{id}")
    public AccountDto getOne(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UUID.fromString(jwt.getClaim("user_id"));

        Account account = accountService.getOne(id, userId);

        AccountDto accDto = mapper.mapTo(account);

        return accDto;
    }

    @GetMapping
    public List<AccountDto> getAll(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaim("user_id"));

        List<Account> accounts = accountService.getAll(userId);

        List<AccountDto> accDto = accounts.stream().map(mapper::mapTo).collect(Collectors.toList());

        return accDto;
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionDto> getTransaction(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UUID.fromString(jwt.getClaim("user_id"));

        List<Transaction> accounts = transactionService.getByAccount(userId, id);

        List<TransactionDto> accDto = accounts.stream().map(transactionMapper::mapTo).collect(Collectors.toList());

        return accDto;
    }
}