import java.io.Serializable;

public abstract class Account implements Serializable {
    private int accountNumber;
    private String accountHolderName;
    protected double balance;

    // Constructor
    public Account(int accountNumber, String accountHolderName, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    // Encapsulation using getters
    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    // Method Overloading (Compile-time Polymorphism)
    public void deposit(double amount) {
        balance += amount;
    }

    public void deposit(double amount, String mode) {
        balance += amount;
        System.out.println("Deposited via " + mode);
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Insufficient Balance");
        }
    }

    // Abstract method
    public abstract void calculateInterest();

    public void displayAccountDetails() {
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Holder Name    : " + accountHolderName);
        System.out.println("Balance        : " + balance);
    }
}


public class SavingsAccount extends Account {

    public SavingsAccount(int accNo, String name, double balance) {
        super(accNo, name, balance);
    }

    // Method Overriding (Runtime Polymorphism)
    @Override
    public void calculateInterest() {
        double interest = balance * 0.04;
        balance += interest;
    }
}


public class CurrentAccount extends Account {

    public CurrentAccount(int accNo, String name, double balance) {
        super(accNo, name, balance);
    }

    @Override
    public void calculateInterest() {
        // No interest for current account
        System.out.println("No interest for Current Account");
    }
}


public class BankManagementSystem {

    static Scanner sc = new Scanner(System.in);
    static List<Account> accounts = new ArrayList<>();
    static final String FILE_NAME = "accounts.dat";

    public static void main(String[] args) {

        loadAccounts();

        while (true) {
            System.out.println("\n--- BANK MENU ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Balance Enquiry");
            System.out.println("5. Display Account Details");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> depositAmount();
                case 3 -> withdrawAmount();
                case 4 -> checkBalance();
                case 5 -> displayAccount();
                case 6 -> {
                    saveAccounts();
                    System.out.println("Thank you for using the Bank System.");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    // Create Account
    static void createAccount() {
        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Initial Balance: ");
        double balance = sc.nextDouble();

        System.out.println("1. Savings Account\n2. Current Account");
        int type = sc.nextInt();

        Account acc;
        if (type == 1) {
            acc = new SavingsAccount(accNo, name, balance);
        } else {
            acc = new CurrentAccount(accNo, name, balance);
        }

        accounts.add(acc);
        System.out.println("Account Created Successfully");
    }

    static Account findAccount(int accNo) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accNo)
                return acc;
        }
        return null;
    }

    static void depositAmount() {
        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();

        Account acc = findAccount(accNo);
        if (acc != null) {
            System.out.print("Enter Amount: ");
            double amt = sc.nextDouble();
            acc.deposit(amt, "Cash");
            acc.calculateInterest();
            System.out.println("Deposit Successful");
        } else {
            System.out.println("Account not found");
        }
    }

    static void withdrawAmount() {
        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();

        Account acc = findAccount(accNo);
        if (acc != null) {
            System.out.print("Enter Amount: ");
            double amt = sc.nextDouble();
            acc.withdraw(amt);
        } else {
            System.out.println("Account not found");
        }
    }

    static void checkBalance() {
        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();

        Account acc = findAccount(accNo);
        if (acc != null) {
            System.out.println("Balance: " + acc.getBalance());
        } else {
            System.out.println("Account not found");
        }
    }

    static void displayAccount() {
        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();

        Account acc = findAccount(accNo);
        if (acc != null) {
            acc.displayAccountDetails();
        } else {
            System.out.println("Account not found");
        }
    }

    // File Handling
    static void saveAccounts() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (Exception e) {
            System.out.println("Error saving data");
        }
    }

    static void loadAccounts() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (List<Account>) ois.readObject();
        } catch (Exception ignored) {}
    }
}