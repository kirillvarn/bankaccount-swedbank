package io.github.kirillvarn.bankaccount.exchange;

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

    public ExchangeController(AccountService accountService, ExchangeService exchangeService, Mapper<Exchange, ExchangeDto> mapper) {
        this.exchangeService = exchangeService;
        this.accountService = accountService;
        this.mapper = mapper;
    };

    @PostMapping
    public ExchangeDto exchange(@AuthenticationPrincipal Jwt jwt, @RequestBody ExchangeRequestDto exchangeDto) {
        String userName = jwt.getClaim("user_id");

        Account fromAccount = accountService.getOne(exchangeDto.getFromAccountId(), userName);
        Account toAccount = accountService.getOne(exchangeDto.getToAccountId(), userName);

        Exchange exchange =
            Exchange.builder()
            .amount(exchangeDto.getAmount())
            .fromAccount(fromAccount)
            .toAccount(toAccount)
            .build();

        Exchange saved = exchangeService.create(exchange);

        return mapper.mapTo(saved);
    }
}
