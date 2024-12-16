package com.caju.service_transaction.service;

import com.caju.service_transaction.entities.TransactionCard;
import com.caju.service_transaction.entities.Wallet;
import com.caju.service_transaction.enums.Category;
import com.caju.service_transaction.model.TransactionRequest;
import com.caju.service_transaction.model.TransactionResponse;
import com.caju.service_transaction.repository.TransactionRepository;
import com.caju.service_transaction.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private MerchantService merchantService;

    @Mock
    private ValidateService validateService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Wallet testWallet;
    private TransactionRequest testRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testWallet = new Wallet();
        testWallet.setId(1L);
        testWallet.setFoodBalance(new BigDecimal("100"));
        testWallet.setCashBalance(new BigDecimal("200"));

        testRequest = new TransactionRequest();
        testRequest.setAccountId(1L);
        testRequest.setAmount(new BigDecimal("50"));
        testRequest.setMcc("5411");
        testRequest.setMerchant("SUPERMARKET");
        testRequest.setPinOrCvv("1234");
    }

    @Test
    void testProcessTransaction_Success() {
        when(validateService.validate(anyLong(), anyString())).thenReturn(true);
        when(walletRepository.findWalletForUpdate(testRequest.getAccountId()))
                .thenReturn(Optional.of(testWallet));
        when(merchantService.mapToCategory(anyString(), anyString()))
                .thenReturn(Category.FOOD);

        TransactionResponse response = transactionService.processTransaction(testRequest);

        assertEquals("00", response.getCode());

        verify(walletRepository).save(testWallet);
        verify(transactionRepository).save(any(TransactionCard.class));
    }

    @Test
    void testProcessTransaction_InvalidPin() {
        when(validateService.validate(anyLong(), anyString())).thenReturn(false);

        TransactionResponse response = transactionService.processTransaction(testRequest);

        assertEquals("07", response.getCode());
        verify(walletRepository, never()).save(testWallet);
        verify(transactionRepository, never()).save(any(TransactionCard.class));
    }

    @Test
    void testProcessTransaction_AccountNotFound() {
        when(validateService.validate(anyLong(), anyString())).thenReturn(true);
        when(walletRepository.findWalletForUpdate(testRequest.getAccountId()))
                .thenReturn(Optional.empty());

        try {
            transactionService.processTransaction(testRequest);
        } catch (RuntimeException ex) {
            assertEquals("Account not found", ex.getMessage());
        }
    }

    @Test
    void testDeductBalance_NotEnoughFunds() {
        testWallet.setFoodBalance(new BigDecimal("10"));
        testWallet.setCashBalance(new BigDecimal("10"));

        when(walletRepository.findWalletForUpdate(anyLong()))
                .thenReturn(Optional.of(testWallet));

        assertFalse(transactionService.deductBalance(testWallet, new BigDecimal("50"), Category.FOOD, testRequest));
    }
}
