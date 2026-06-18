package io.github.kirillvarn.bankaccount.transaction;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
}