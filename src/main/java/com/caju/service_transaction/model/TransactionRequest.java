package com.caju.service_transaction.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    @NotBlank
    @Schema(example = "1")
    private Long accountId;

    @NonNull
    @Schema(example = "10.00")
    private BigDecimal amount;

    @NonNull
    @Schema(example = "5411")
    private String mcc;

    @NonNull
    @Schema(example = "Mercado do ze")
    private String merchant;

    @NonNull
    @Schema(example = "1111")
    private String pinOrCvv;

}
