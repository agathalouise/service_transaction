package com.caju.service_transaction.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    @NotBlank
    private Long accountId;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private String mcc;
    @NonNull
    private String merchant;
    @NonNull
    private String pinOrCvv;

    public boolean isPin() {
        return pinOrCvv.length() == 4 || pinOrCvv.length() == 6;
    }

}
