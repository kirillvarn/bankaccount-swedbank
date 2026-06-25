package io.github.kirillvarn.bankaccount.account;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public AccountController(Mapper<Transaction, TransactionDto> transactionMapper,
            TransactionService transactionService, AccountService accountService, Mapper<Account, AccountDto> mapper) {
        this.accountService = accountService;
        this.transactionMapper = transactionMapper;
        this.transactionService = transactionService;
        this.mapper = mapper;
    }

    @PostMapping
    public AccountDto create(@AuthenticationPrincipal Jwt jwt, @RequestBody AccountDto accountDto) {
        Account acc = mapper.mapFrom(accountDto);
        String userName = jwt.getClaim("user_id");
        Account saved = accountService.create(acc, userName);
        return mapper.mapTo(saved);
    }

    @GetMapping("/{id}")
    public AccountDto getOne(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        String userName = jwt.getClaim("user_id");

        Account account = accountService.getOne(id, userName);

        AccountDto accDto = mapper.mapTo(account);

        return accDto;
    }

    @GetMapping
    public List<AccountDto> getAll(@AuthenticationPrincipal Jwt jwt) {
        String userName = jwt.getClaim("user_id");

        List<Account> accounts = accountService.getAll(userName);

        List<AccountDto> accDto = accounts.stream().map(mapper::mapTo).collect(Collectors.toList());

        return accDto;
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionDto> getTransaction(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int limit) {

        String userName = jwt.getClaim("user_id");

        List<Transaction> transactions = transactionService.getByAccount(userName, id, page, limit);

        List<TransactionDto> transactionDto = transactions.stream().map(transactionMapper::mapTo).collect(Collectors.toList());

        return transactionDto;
    }
}