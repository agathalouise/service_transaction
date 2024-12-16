package com.caju.service_transaction.service;

import com.caju.service_transaction.repository.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ValidateService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ValidateService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean validate(Long accountId, String pinOrCvv) {
        var isPin = isPin(pinOrCvv);
        return accountRepository.findById(accountId)
                .map(accountSecurity -> {
                    String storedHash = isPin ? accountSecurity.getHashedPin() : accountSecurity.getHashedCvv();
                    return passwordEncoder.matches(pinOrCvv, storedHash);
                })
                .orElse(false);

    }

    public boolean isPin(String pinOrCvv) {
        return pinOrCvv.length() == 4 || pinOrCvv.length() == 6;
    }

}
