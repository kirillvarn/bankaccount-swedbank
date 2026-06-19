package io.github.kirillvarn.bankaccount.transaction;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import io.github.kirillvarn.bankaccount.Mapper;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private TransactionService transactionService;
    private Mapper<Transaction, TransactionDto> mapper;

    @Autowired
    public TransactionController(TransactionService transactionService, Mapper<Transaction, TransactionDto> mapper) {
        this.transactionService = transactionService;
        this.mapper = mapper;
    };

    @PostMapping
    public TransactionDto create(@AuthenticationPrincipal Jwt jwt, @RequestBody TransactionDto transactionDto) {
        Transaction transaction = mapper.mapFrom(transactionDto);

        UUID userId = UUID.fromString(jwt.getClaim("user_id"));

        Transaction saved = transactionService.create(transaction, userId);
        return mapper.mapTo(saved);
    };
}