package com.caju.service_transaction.controller;


import com.caju.service_transaction.model.TransactionRequest;
import com.caju.service_transaction.model.TransactionResponse;
import com.caju.service_transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;

    private TransactionRequest transactionRequest;
    private TransactionResponse transactionResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionController = new TransactionController(transactionService);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();

        transactionRequest = new TransactionRequest();
        transactionRequest.setAccountId(1L);
        transactionRequest.setAmount(BigDecimal.valueOf(100.0));
        transactionRequest.setMcc("1234");
        transactionRequest.setMerchant("Uber Trip");
        transactionRequest.setPinOrCvv("1234");

        transactionResponse = new TransactionResponse("00");
    }

    @Test
    void testAuthorizeTransaction_Success() throws Exception {
        when(transactionService.processTransaction(transactionRequest)).thenReturn(transactionResponse);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(transactionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));

        verify(transactionService, times(1)).processTransaction(transactionRequest);
    }

    @Test
    void testAuthorizeTransaction_BadRequest() throws Exception {
        when(transactionService.processTransaction(transactionRequest)).thenReturn(new TransactionResponse("51"));

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(transactionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("51"));

        verify(transactionService, times(1)).processTransaction(transactionRequest);
    }
}
