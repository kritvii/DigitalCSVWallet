#  DigitalCSVWallet

**DigitalCSVWallet** is a simple Java-based wallet management application that uses **CSV files** for data storage. (`data/users.csv`, `data/transactions.csv`).  
It allows users to store, view, and manage transactions in a lightweight, file-based digital wallet — without needing a full database setup.

---

##  Features
-  Register/Login with **User ID + PIN**
-  Deposit, Withdraw, and Transfer between users
-  Transaction history with table display
-  Persistent data storage using **CSV files** (no database needed)
-  Auto-creates CSV files on first run
-  Simple console or Swing-based interface

---

## How to Run (Mac / Windows / Linux)

### Using `javac` and `java`
```bash
cd DigitalWalletJavaCSV_fixed
javac -d out $(find src -name "*.java")
cd out
java com.walletapp.Main
```

**Windows PowerShell**
```powershell
cd DigitalWalletJavaCSV_fixed
Get-ChildItem -Recurse -Filter *.java | % { $_.FullName } | javac -d out - @
cd out
java com.walletapp.Main
```

> Requires **Java 8+** (for `java.time` API).

---

###  In VS Code
1. Open the folder `DigitalWalletJavaCSV_fixed`.
2. Install **Extension Pack for Java**.
3. Run `Main.java` directly or configure a Java build task.
4. Ensure your working directory is the project root — so the `data/` folder is created beside the compiled output.

---

##  Project Structure
```
DigitalCSVWallet/
├── src/                # Java source code
│   └── com/walletapp/  # Main app package (Main.java, etc.)
├── data/               # Transaction CSV files
├── out/                # Compiled class files
└── README.md           # Project documentation
```

** Detailed layout:**
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

---

##  Default Data
- The app auto-creates empty CSVs on first run.  
- Register **two users** to test the **Transfer** feature.

---

##  Example CSV Format

A sample `data/transactions.csv` might look like:
```csv
Date,Description,Amount,Type
2025-10-25,Deposit,5000,Credit
2025-10-26,Online Purchase,-1200,Debit
```

---

##  Future Enhancements
- GUI redesign using JavaFX or Swing components  
- CSV encryption for PINs and transactions  
- Export statements as PDF  
- Integration with analytics APIs  
- Add dark/light mode for UI  

---


## Developed by:
**Kritvi Rawat**   
16014024060

**Meeta Patil**   
16014024055

---

