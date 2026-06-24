package io.github.kirillvarn.bankaccount.exchange;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.kirillvarn.bankaccount.Mapper;
import io.github.kirillvarn.bankaccount.account.Account;
import io.github.kirillvarn.bankaccount.account.AccountService;


@RestController
@RequestMapping("/api/v1/exchanges")
public class ExchangeController {

    private ExchangeService exchangeService;
    private AccountService accountService;
    private Mapper<Exchange, ExchangeDto> mapper;

    @Autowired
    public ExchangeController(AccountService accountService, ExchangeService exchangeService, Mapper<Exchange, ExchangeDto> mapper) {
        this.exchangeService = exchangeService;
        this.accountService = accountService;
        this.mapper = mapper;
    };

    @PostMapping
    public ExchangeDto exchange(@AuthenticationPrincipal Jwt jwt, @RequestBody ExchangeRequestDto exchangeDto) {
        UUID userId = UUID.fromString(jwt.getClaim("user_id"));

        Account fromAccount = accountService.getOne(exchangeDto.getFromAccountId(), userId);
        Account toAccount = accountService.getOne(exchangeDto.getToAccountId(), userId);

        Exchange exchange =
            Exchange.builder()
            .amount(exchangeDto.getAmount())
            .fromAccount(fromAccount)
            .toAccount(toAccount)
            .build();

        Exchange saved = exchangeService.create(exchange, userId);

        return mapper.mapTo(saved);
    }
}
