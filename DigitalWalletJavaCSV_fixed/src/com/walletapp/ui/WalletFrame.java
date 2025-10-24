package com.walletapp.ui;

import com.walletapp.service.WalletService;
import com.walletapp.model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class WalletFrame extends JFrame {
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private final WalletService service = new WalletService();

    // login
    private JTextField userIdField;
    private JPasswordField pinField;
    // dashboard
    private JLabel welcomeLabel;
    private JLabel balanceLabel;
    private JTable txTable;

    private String currentUserId;

    public WalletFrame() {
        super("Digital Banking Wallet (CSV)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        root.add(buildLoginPanel(), "login");
        root.add(buildDashboardPanel(), "dash");
        setContentPane(root);

        cards.show(root, "login");
    }

    private JPanel buildLoginPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8,8,8,8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login / Register");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        gc.gridx=0; gc.gridy=0; gc.gridwidth=2;
        p.add(title, gc);

        gc.gridwidth=1;
        gc.gridy++; gc.gridx=0;
        p.add(new JLabel("User ID:"), gc);
        userIdField = new JTextField(20);
        gc.gridx=1;
        p.add(userIdField, gc);

        gc.gridy++; gc.gridx=0;
        p.add(new JLabel("PIN:"), gc);
        pinField = new JPasswordField(20);
        gc.gridx=1;
        p.add(pinField, gc);

        JButton loginBtn = new JButton("Login");
        JButton regBtn = new JButton("Register");
        JPanel actions = new JPanel();
        actions.add(loginBtn);
        actions.add(regBtn);
        gc.gridy++; gc.gridx=0; gc.gridwidth=2;
        p.add(actions, gc);

        loginBtn.addActionListener(e -> onLogin());
        regBtn.addActionListener(e -> onRegister());

        return p;
    }

    private JPanel buildDashboardPanel() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        JPanel top = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 18f));
        top.add(welcomeLabel, BorderLayout.WEST);

        balanceLabel = new JLabel("Balance: 0.00");
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        top.add(balanceLabel, BorderLayout.EAST);

        p.add(top, BorderLayout.NORTH);

        // Center: transactions table
        txTable = new JTable(new DefaultTableModel(new Object[]{"Time","Type","Amount","Note","Balance After"}, 0));
        JScrollPane sp = new JScrollPane(txTable);
        p.add(sp, BorderLayout.CENTER);

        // Bottom: actions
        JPanel actions = new JPanel();
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton transferBtn = new JButton("Transfer");
        JButton refreshBtn = new JButton("Refresh");
        JButton logoutBtn = new JButton("Logout");
        actions.add(depositBtn);
        actions.add(withdrawBtn);
        actions.add(transferBtn);
        actions.add(refreshBtn);
        actions.add(logoutBtn);
        p.add(actions, BorderLayout.SOUTH);

        depositBtn.addActionListener(e -> doDeposit());
        withdrawBtn.addActionListener(e -> doWithdraw());
        transferBtn.addActionListener(e -> doTransfer());
        refreshBtn.addActionListener(e -> refresh());
        logoutBtn.addActionListener(e -> {
            currentUserId = null;
            ((DefaultTableModel)txTable.getModel()).setRowCount(0);
            userIdField.setText("");
            pinField.setText("");
            cards.show(getContentPane(), "login");
        });

        return p;
    }

    private void onLogin() {
        String uid = userIdField.getText().trim();
        String pin = new String(pinField.getPassword());
        if (uid.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter both User ID and PIN");
            return;
        }
        service.authenticate(uid, pin).ifPresentOrElse(u -> {
            currentUserId = uid;
            welcomeLabel.setText("Welcome, " + service.getName(uid) + " (" + uid + ")");
            refresh();
            cards.show(getContentPane(), "dash");
        }, () -> JOptionPane.showMessageDialog(this, "Invalid credentials"));
    }

    private void onRegister() {
        String uid = userIdField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();
        if (uid.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Provide a new User ID and a PIN to register");
            return;
        }
        String name = JOptionPane.showInputDialog(this, "Enter your name:");
        if (name == null || name.trim().isEmpty()) return;
        boolean ok = service.register(uid, name.trim(), pin);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Registered! You can log in now.");
        } else {
            JOptionPane.showMessageDialog(this, "User ID already exists.");
        }
    }

    private void refresh() {
        if (currentUserId == null) return;
        balanceLabel.setText(String.format("Balance: %.2f", service.getBalance(currentUserId)));
        List<Transaction> txs = service.transactions(currentUserId);
        DefaultTableModel m = (DefaultTableModel) txTable.getModel();
        m.setRowCount(0);
        for (Transaction t : txs) {
            m.addRow(new Object[]{ t.getTimestamp().toString(), t.getType(), t.getAmount(), t.getNote(), t.getBalanceAfter() });
        }
    }

    private void doDeposit() {
        if (currentUserId == null) return;
        String amt = JOptionPane.showInputDialog(this, "Amount to deposit:");
        if (amt == null) return;
        try {
            double a = Double.parseDouble(amt);
            String note = JOptionPane.showInputDialog(this, "Note (optional):");
            service.deposit(currentUserId, a, note);
            refresh();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void doWithdraw() {
        if (currentUserId == null) return;
        String amt = JOptionPane.showInputDialog(this, "Amount to withdraw:");
        if (amt == null) return;
        try {
            double a = Double.parseDouble(amt);
            String note = JOptionPane.showInputDialog(this, "Note (optional):");
            boolean ok = service.withdraw(currentUserId, a, note);
            if (!ok) JOptionPane.showMessageDialog(this, "Insufficient balance");
            refresh();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void doTransfer() {
        if (currentUserId == null) return;
        String to = JOptionPane.showInputDialog(this, "Transfer to User ID:");
        if (to == null || to.trim().isEmpty()) return;
        String amt = JOptionPane.showInputDialog(this, "Amount:");
        if (amt == null) return;
        try {
            double a = Double.parseDouble(amt);
            String note = JOptionPane.showInputDialog(this, "Note (optional):");
            boolean ok = service.transfer(currentUserId, to.trim(), a, note);
            if (!ok) JOptionPane.showMessageDialog(this, "Failed (check recipient exists and balance)");
            refresh();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount");
        }
    }
}