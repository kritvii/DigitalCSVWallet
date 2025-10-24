package com.walletapp.model;

import java.time.LocalDateTime;

public class Transaction {
    private LocalDateTime timestamp;
    private String userId;
    private String type; // DEPOSIT, WITHDRAW, TRANSFER_OUT, TRANSFER_IN
    private double amount;
    private String note;
    private double balanceAfter;

    public Transaction(LocalDateTime timestamp, String userId, String type, double amount, String note, double balanceAfter) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.balanceAfter = balanceAfter;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getUserId() { return userId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getNote() { return note; }
    public double getBalanceAfter() { return balanceAfter; }
}