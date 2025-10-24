package com.walletapp.service;

import com.walletapp.model.User;
import java.util.ArrayList;
import java.util.List;

public class WalletService {
    private List<User> users;

    public WalletService() {
        users = new ArrayList<>();
    }

    // Add a new user
    public void addUser(User user) {
        users.add(user);
    }

    // Find a user by ID
    public User findUserById(String id) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    // Deposit amount to user wallet
    public boolean deposit(String userId, double amount) {
        User user = findUserById(userId);
        if (user != null && amount > 0) {
            user.deposit(amount);
            return true;
        }
        return false;
    }

    // Withdraw amount from user wallet
    public boolean withdraw(String userId, double amount) {
        User user = findUserById(userId);
        if (user != null && amount > 0) {
            return user.withdraw(amount);
        }
        return false;
    }

    // Transfer amount between users
    public boolean transfer(String fromUserId, String toUserId, double amount) {
        User fromUser = findUserById(fromUserId);
        User toUser = findUserById(toUserId);
        if (fromUser != null && toUser != null && amount > 0) {
            if (fromUser.withdraw(amount)) {
                toUser.deposit(amount);
                return true;
            }
        }
        return false;
    }

    // List all users (for debugging)
    public void listUsers() {
        System.out.println("=== Users ===");
        for (User u : users) {
            System.out.println(u);
        }
    }
}
