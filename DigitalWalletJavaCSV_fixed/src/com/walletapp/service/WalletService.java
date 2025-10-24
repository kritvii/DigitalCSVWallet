package com.walletapp.service;

import java.util.List;

public class WalletService {

    private Persistence persistence;

    public WalletService() {
        persistence = new Persistence();
    }

    // Example method
    public void addUser(User user) {
        try {
            List<User> users = persistence.loadUsers();
            users.add(user);
            persistence.saveUsers(users);
        } 
        // Removed unnecessary catch(IOException e)
        catch (Exception e) { // Use a generic Exception if saveUsers/loadUsers can throw
            e.printStackTrace();
        }
    }

    public User login(String id, String password) {
        try {
            List<User> users = persistence.loadUsers();
            for (User u : users) {
                if (u.getId().equals(id) && u.getPassword().equals(password)) {
                    return u;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // login failed
    }

    // Any other methods
    public void printAllUsers() {
        try {
            List<User> users = persistence.loadUsers();
            for (User u : users) {
                System.out.println(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
