package com.caju.service_transaction.service;

import com.caju.service_transaction.entities.Account;
import com.caju.service_transaction.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class ValidateServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ValidateService validateService;

    private Account mockAccount;

    private BCryptPasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validateService = new ValidateService(accountRepository);
        passwordEncoder = new BCryptPasswordEncoder();
        mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setHashedPin(passwordEncoder.encode("1234"));
        mockAccount.setHashedCvv(passwordEncoder.encode("567"));
    }

    @Test
    void testValidateWithCorrectPin() {
        Long accountId = 1L;
        String pin = "1234";

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        boolean result = validateService.validate(accountId, pin);
        boolean isValidPassword = passwordEncoder.matches(pin, mockAccount.getHashedPin());

        assertTrue(result, "The validation should succeed with the correct pin.");
        assertTrue(isValidPassword);
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void testValidateWithIncorrectPin() {
        Long accountId = 1L;
        String pin = "9999";

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        boolean result = validateService.validate(accountId, pin);

        assertFalse(result, "The validation should fail with an incorrect pin.");
    }

    @Test
    void testValidateWithCorrectCvv() {
        Long accountId = 1L;
        String cvv = "567";

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        boolean result = validateService.validate(accountId, cvv);

        assertTrue(result, "The validation should succeed with the correct CVV.");
    }

    @Test
    void testValidateWithIncorrectCvv() {
        Long accountId = 1L;
        String cvv = "166";

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        boolean result = validateService.validate(accountId, cvv);

        assertFalse(result, "The validation should fail with an incorrect CVV.");
    }

    @Test
    void testValidateWithAccountNotFound() {
        Long accountId = 12L;
        String pinOrCvv = "1234";

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        boolean result = validateService.validate(accountId, pinOrCvv);

        assertFalse(result, "The validation should fail if the account is not found.");
    }

    @Test
    void testIsPinWith4Characters() {
        String input = "1234";

        boolean result = validateService.isPin(input);

        assertTrue(result, "Should identify a 4-character input as a PIN.");
    }

    @Test
    void testIsPinWith6Characters() {
        String input = "123456";

        boolean result = validateService.isPin(input);

        assertTrue(result, "Should identify a 6-character input as a PIN.");
    }

    @Test
    void testIsPinWithInvalidLength() {
        String input = "123453453453";

        boolean result = validateService.isPin(input);

        assertFalse(result, "Should not identify an input with less than 4 or more than 6 characters as a PIN.");
    }

}
