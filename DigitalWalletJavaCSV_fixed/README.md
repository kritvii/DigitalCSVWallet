# Digital Banking Wallet (CSV) — Fixed

A simple Swing-based Java project that stores users and transactions in CSV files (`data/users.csv`, `data/transactions.csv`).

## Features
- Register/Login with user ID + PIN
- Deposit, Withdraw, Transfer between users
- Transaction history table
- Data saved in CSV (no external DB)

## How to Run (Mac/Windows)

### Using `javac` and `java`
```bash
cd DigitalWalletJavaCSV_fixed
javac -d out $(find src -name "*.java")
cd out
java com.walletapp.Main
```

Windows PowerShell:
```powershell
cd DigitalWalletJavaCSV_fixed
Get-ChildItem -Recurse -Filter *.java | % { $_.FullName } | javac -d out - @
cd out
java com.walletapp.Main
```

> Ensure you are using Java 8+ (for `java.time` API).

### In VS Code
1. Open the folder `DigitalWalletJavaCSV_fixed`.
2. Install "Extension Pack for Java".
3. Create a project build task or use Java: Compile/Run from the editor on `Main.java`.
4. Make sure your working directory is the project root so the `data` folder is created next to the executable.

### Code::Blocks on Windows (optional UI IDE)
Code::Blocks is primarily for C/C++; for Java, prefer VS Code or IntelliJ. If you must use Code::Blocks, configure a custom build command to call `javac` and `java` as above.

## Default Data
- The app creates empty CSVs on first run.
- Register at least **two** users to try **Transfer**.

## Project Layout
```
src/com/walletapp/Main.java
src/com/walletapp/ui/WalletFrame.java
src/com/walletapp/model/User.java
src/com/walletapp/model/Transaction.java
src/com/walletapp/service/Persistence.java
src/com/walletapp/service/WalletService.java
data/users.csv
data/transactions.csv
```
