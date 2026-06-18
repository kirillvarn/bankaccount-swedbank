package io.github.kirillvarn.bankaccount.account;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private UUID user_id;
    private String currency;
    private double balance;

    public Account(UUID user_id, String currency, double balance) {
        this.user_id = user_id;
        this.currency = currency;
        this.balance = balance;
    };

    public double getBalance() {
        return this.balance;
    }

    public String getCurrency() {
        return this.currency;
    }

    public UUID getUserId() {
        return this.user_id;
    }
}