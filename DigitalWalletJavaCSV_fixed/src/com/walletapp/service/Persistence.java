package com.walletapp.service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

import com.walletapp.model.Transaction;
import com.walletapp.model.User;

public class Persistence {

    private final Path dataDir;
    private final Path userFile;
    private final Path txnFile;

    public Persistence(Path dataDir) {
        this.dataDir = dataDir;
        this.userFile = dataDir.resolve("users.csv");
        this.txnFile = dataDir.resolve("transactions.csv");

        try {
            if (!Files.exists(dataDir)) Files.createDirectories(dataDir);
            if (!Files.exists(userFile)) Files.createFile(userFile);
            if (!Files.exists(txnFile)) Files.createFile(txnFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------- USERS ----------------

    public Map<String, User> loadUsers() {
        Map<String, User> users = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(userFile)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] a = parseCsvLine(line);
                if (a.length >= 4) {
                    User u = new User(a[0], a[1], Double.parseDouble(a[2]), a[3]);
                    users.put(a[1], u);
                }
            }
        } catch (IOException e) {
            // ignore if empty
        }
        return users;
    }

    public void saveUsers(Map<String, User> users) {
        try (BufferedWriter bw = Files.newBufferedWriter(userFile)) {
            for (User u : users.values()) {
                bw.write(escapeCsv(u.getName()) + "," +
                         escapeCsv(u.getId()) + "," +
                         u.getBalance() + "," +
                         escapeCsv(u.getPassword()));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------- TRANSACTIONS ----------------

    public List<Transaction> loadTransactionsByUser(String userId) {
        List<Transaction> txns = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(txnFile)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] a = parseCsvLine(line);
                if (a.length >= 6 && a[1].equals(userId)) {
                    Transaction t = new Transaction(
                        LocalDateTime.parse(a[0]), a[1], a[2],
                        Double.parseDouble(a[3]), a[4], Double.parseDouble(a[5])
                    );
                    txns.add(t);
                }
            }
        } catch (IOException e) {
            // ignore if none exist yet
        }
        return txns;
    }

    public void appendTransaction(Transaction t) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(txnFile.toFile(), true))) {
            bw.write(escapeCsv(t.getTimestamp().toString()) + "," +
                     escapeCsv(t.getUserId()) + "," +
                     escapeCsv(t.getType()) + "," +
                     t.getAmount() + "," +
                     escapeCsv(t.getNote()) + "," +
                     t.getBalanceAfter());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------- CSV HELPERS ----------------

    private static String escapeCsv(String s) {
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }

    private static String[] parseCsvLine(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '\"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                        sb.append('\"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    sb.append(c);
                }
            } else {
                if (c == '\"') {
                    inQuotes = true;
                } else if (c == ',') {
                    out.add(sb.toString());
                    sb.setLength(0);
                } else {
                    sb.append(c);
                }
            }
        }
        out.add(sb.toString());
        return out.toArray(new String[0]);
    }
}
