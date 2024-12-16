package com.caju.service_transaction.service;

import com.caju.service_transaction.enums.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MerchantServiceTest {

    private MerchantService merchantService;

    @BeforeEach
    void setUp() {
        merchantService = new MerchantService();
    }

    @Test
    void testMapToCategory_UberTrip() {
        String mcc = "1234";
        String merchant = "Uber Trip";

        Category category = merchantService.mapToCategory(mcc, merchant);

        assertEquals(Category.TRANSPORT, category);
    }


    @Test
    void testMapToCategory_Ifood() {
        String mcc = "5812";
        String merchant = "iFood";

        Category category = merchantService.mapToCategory(mcc, merchant);

        assertEquals(Category.MEAL, category);
    }

    @Test
    void testMapToCategory_Mercado() {
        String mcc = "5811";
        String merchant = "Supermercado";

        Category category = merchantService.mapToCategory(mcc, merchant);

        assertEquals(Category.FOOD, category);
    }

    @Test
    void testMapToCategory_Livraria() {
        String mcc = "1234";
        String merchant = "Livraria";

        Category category = merchantService.mapToCategory(mcc, merchant);

        assertEquals(Category.CULTURE, category);
    }

    @Test
    void testMapToCategory_DefaultMcc() {
        String mcc = "5411";
        String merchant = "Unknown Merchant";

        Category category = merchantService.mapToCategory(mcc, merchant);

        assertEquals(Category.FOOD, category);
    }
}
