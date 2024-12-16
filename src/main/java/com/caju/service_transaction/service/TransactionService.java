package com.caju.service_transaction.service;

import com.caju.service_transaction.entities.TransactionCard;
import com.caju.service_transaction.entities.Wallet;
import com.caju.service_transaction.enums.Category;
import com.caju.service_transaction.model.TransactionRequest;
import com.caju.service_transaction.model.TransactionResponse;
import com.caju.service_transaction.repository.TransactionRepository;
import com.caju.service_transaction.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static com.caju.service_transaction.enums.Category.CASH;


@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final WalletRepository walletRepository;
    private final MerchantService merchantService;
    private final ValidateService validateService;
    private final TransactionRepository transactionRepository;

    @Transactional //L4 - Garante que todas as operações sejam executadas em uma transação única e atômica
    public TransactionResponse processTransaction(TransactionRequest request) {

        // Validate PIN or CVV
        if (!validateService.validate(request.getAccountId(), request.getPinOrCvv(), request.isPin())) {
            log.info("pin or cvv invalid");
            return new TransactionResponse("07");
        }

        Wallet wallet = walletRepository.findWalletForUpdate(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Category category = merchantService.mapToCategory(request.getMcc(), request.getMerchant());

        // Deduct balance based on category
        boolean success = deductBalance(wallet, request.getAmount(), category, request);

        return new TransactionResponse(success ? "00" : "51");
    }


    private boolean deductBalance(Wallet wallet, BigDecimal amount, Category category, TransactionRequest request) {
        BigDecimal categoryBalance = getCategoryBalance(wallet, category);

        if (categoryBalance.compareTo(amount) >= 0) {
            Map<Category, BigDecimal> balancesToUpdate = Map.of(category, categoryBalance.subtract(amount));
            updateWalletBalances(wallet, balancesToUpdate, request);
            return true;
        } else {
            BigDecimal remainingAmount = amount.subtract(categoryBalance);
            if (wallet.getCashBalance().compareTo(remainingAmount) >= 0) {
                log.info("Insufficient balance in category '{}'. Using CASH balance to complete: {} required from total balance.", category, amount);
                Map<Category, BigDecimal> balancesToUpdate = Map.of(
                        category, BigDecimal.ZERO,
                        CASH, wallet.getCashBalance().subtract(remainingAmount)
                );

                updateWalletBalances(wallet, balancesToUpdate, request);
                return true;
            }

            log.info("Not enough funds available");
            return false;
        }
    }

    private BigDecimal getCategoryBalance(Wallet wallet, Category category) {
        return switch (category) {
            case FOOD -> wallet.getFoodBalance();
            case MEAL -> wallet.getMealBalance();
            case CULTURE -> wallet.getCultureBalance();
            case TRANSPORT -> wallet.getTransportBalance();
            default -> wallet.getCashBalance();
        };
    }

    private void updateWalletBalances(Wallet wallet, Map<Category, BigDecimal> balancesToUpdate, TransactionRequest request) {
        balancesToUpdate.forEach((category, newBalance) -> {
            switch (category) {
                case FOOD -> wallet.setFoodBalance(newBalance);
                case MEAL -> wallet.setMealBalance(newBalance);
                case CULTURE -> wallet.setCultureBalance(newBalance);
                case TRANSPORT -> wallet.setTransportBalance(newBalance);
                default -> wallet.setCashBalance(newBalance);
            }
        });

        saveTransaction(wallet, request);
        walletRepository.save(wallet);
    }

    private void saveTransaction(Wallet wallet, TransactionRequest request) {
        TransactionCard transaction = new TransactionCard();
        transaction.setAmount(request.getAmount());
        transaction.setMcc(request.getMcc());
        transaction.setMerchant(request.getMerchant());
        transaction.setCreatedDate(LocalDateTime.now());
        transaction.setWallet(wallet);

        transactionRepository.save(transaction);
        log.info("Transaction saved to database.");

        walletRepository.save(wallet);
        log.info("Wallet balance updated");
    }


}
