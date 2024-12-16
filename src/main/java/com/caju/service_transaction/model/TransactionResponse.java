package com.caju.service_transaction.model;

public class TransactionResponse {

    private String code;

    public TransactionResponse(String code) {
        this.code = code;
    }

    public TransactionResponse() {
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
