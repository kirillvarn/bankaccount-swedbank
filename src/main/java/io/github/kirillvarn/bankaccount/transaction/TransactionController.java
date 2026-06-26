package io.github.kirillvarn.bankaccount.transaction;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import io.github.kirillvarn.bankaccount.Mapper;
import io.github.kirillvarn.bankaccount.transaction.Transaction.TransactionType;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    static final String EXTERNAL_URL = "https://tools-httpstatus.pickup-services.com/200";

    private TransactionService transactionService;
    private Mapper<Transaction, TransactionDto> mapper;
    private RestTemplate restTemplate;

    public TransactionController(RestTemplate restTemplate, TransactionService transactionService, Mapper<Transaction, TransactionDto> mapper) {
        this.transactionService = transactionService;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    };

    @PostMapping
    public TransactionDto create(@RequestBody TransactionDto transactionDto) {
        if (transactionDto.getTransactionType() == TransactionType.DEB) {
            simulateExternalSystemCall();
        }

        Transaction transaction = mapper.mapFrom(transactionDto);

        Transaction saved = transactionService.create(transaction);
        return mapper.mapTo(saved);
    };

    @GetMapping("/{id}")
    public TransactionDto getOne(@PathVariable UUID id) {
        Optional<Transaction> transaction = transactionService.getOne(id);

        if (!transaction.isPresent())
            return null;

        TransactionDto transactionDto = mapper.mapTo(transaction.get());

        return transactionDto;
    }

    private void simulateExternalSystemCall() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(EXTERNAL_URL, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                System.err.println("Not 200 HTTP code");
            }
        } catch (Exception e) {
            System.err.println("Error");
        }
    }
}