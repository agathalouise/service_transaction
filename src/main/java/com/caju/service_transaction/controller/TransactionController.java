package com.caju.service_transaction.controller;

import com.caju.service_transaction.model.TransactionRequest;
import com.caju.service_transaction.model.TransactionResponse;
import com.caju.service_transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction", description = "Transaction Authorization API")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @Operation(summary = "Authorize a transaction", description = "Processes a transaction request and returns the authorization result.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction processed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<TransactionResponse> authorizeTransaction(@RequestBody @Valid TransactionRequest request) {
        TransactionResponse response = transactionService.processTransaction(request);
        return ResponseEntity.ok(response);
    }
}

