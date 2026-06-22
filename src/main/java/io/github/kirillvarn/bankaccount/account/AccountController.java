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

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private AccountService accountService;
    private Mapper<Account, AccountDto> mapper;

    @Autowired
    public AccountController(AccountService accountService, Mapper<Account, AccountDto> mapper) {
        this.accountService = accountService;
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
}