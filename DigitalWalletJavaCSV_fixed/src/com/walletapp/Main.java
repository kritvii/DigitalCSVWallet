package com.walletapp;

import com.walletapp.ui.WalletFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WalletFrame().setVisible(true);
        });
    }
}