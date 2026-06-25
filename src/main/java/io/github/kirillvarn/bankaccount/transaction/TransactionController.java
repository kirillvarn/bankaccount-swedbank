package io.github.kirillvarn.bankaccount.transaction;

import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import io.github.kirillvarn.bankaccount.Mapper;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private TransactionService transactionService;
    private Mapper<Transaction, TransactionDto> mapper;

    public TransactionController(TransactionService transactionService, Mapper<Transaction, TransactionDto> mapper) {
        this.transactionService = transactionService;
        this.mapper = mapper;
    };

    @PostMapping
    public TransactionDto create(@RequestBody TransactionDto transactionDto) {
        Transaction transaction = mapper.mapFrom(transactionDto);

        Transaction saved = transactionService.create(transaction);
        return mapper.mapTo(saved);
    };

    @GetMapping("/{id}")
    public TransactionDto getOne(@PathVariable UUID id) {
        Optional<Transaction> transaction = transactionService.getOne(id);

        if (!transaction.isPresent()) return null;

        TransactionDto transactionDto = mapper.mapTo(transaction.get());

        return transactionDto;
    }
}