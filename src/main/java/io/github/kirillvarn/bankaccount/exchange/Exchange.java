package io.github.kirillvarn.bankaccount.exchange;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exchanges")
public class Exchange {
    public enum Currency {
        EUR,
        USD,
        SEK,
        GBP,
        VND
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency fromCurrency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency toCurrency;

    private BigDecimal exchangeRate;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp createdAt;
}