package com.caju.service_transaction.service;

import com.caju.service_transaction.enums.Category;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {
    public Category mapToCategory(String mcc, String merchant) {
        var merchantUC = merchant.toUpperCase();
        if (merchantUC.contains("UBER TRIP") || merchantUC.contains("BILHETEUNICO")) {
            return Category.TRANSPORT;
        } else if (merchantUC.contains("PADARIA") || merchantUC.contains("IFOOD") || merchantUC.contains("UBER EATS")) {
            return Category.MEAL;
        } else if (merchantUC.contains("MERCADO") || merchantUC.contains("MERCEARIA")) {
            return Category.FOOD;
        } else if (merchantUC.contains("LIVRARIA") || merchantUC.contains("CINEMA")) {
            return Category.CULTURE;
        }

        return switch (mcc) {
            case "5411", "5412" -> Category.FOOD;
            case "5811", "5812" -> Category.MEAL;
            default -> Category.CASH;
        };
    }
}
