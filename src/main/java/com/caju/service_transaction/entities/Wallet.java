package com.caju.service_transaction.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
public class Wallet implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal foodBalance;
    private BigDecimal mealBalance;
    private BigDecimal cultureBalance;
    private BigDecimal transportBalance;
    private BigDecimal cashBalance;

    @OneToOne(mappedBy = "wallet", cascade = CascadeType.ALL)
    private Account account;
}

