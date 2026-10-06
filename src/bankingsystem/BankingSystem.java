package bankingsystem;



import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class BankingSystem {
public static void main(String[] args){
 Bank bank = new Bank();
 Scanner sc = new Scanner(System.in);

 bank.addAccount(new SavingsAccount("1001", "John", 5000));
 bank.addAccount(new ChequeAccount("1002", "Mike", 2500));

 while(true){
  System.out.println("\n=== BANKING SYSTEM ===");
 System.out.println("1. Create Account");
  System.out.println("2. Deposit");
 System.out.println("3. Withdraw");
  System.out.println("4. Display All Sorted");
 System.out.println("5. GUI Version");
  System.out.println("6. Exit");
 System.out.print("Choose: ");

 int choice = 0;
 try{
  choice = Integer.parseInt(sc.nextLine());
 }catch(Exception e){
  System.out.println("Please enter a number");

 }

 if(choice == 1){
  try{
  System.out.print("Account Number: ");
  String num = sc.nextLine();
  System.out.print("Owner Name: ");
  String name = sc.nextLine();
 System.out.print("Initial Balance: ");
  double bal = Double.parseDouble(sc.nextLine());
  System.out.print("Type 1=SAVINGS 2=CHEQUE: ");
  int t = Integer.parseInt(sc.nextLine());

  Account acc = (t==1)? new SavingsAccount(num,name,bal) : new ChequeAccount(num,name,bal);
  bank.addAccount(acc);
 System.out.println("Account created!");

  }catch(Exception e){
  System.out.println("Invalid input");

  }
 }else if(choice == 2){
 System.out.print("Account Number: ");
  Account acc = bank.findAccount(sc.nextLine());

  if(acc!= null){
  System.out.print("Amount: ");

  try{ double amt = Double.parseDouble(sc.nextLine()); acc.deposit(amt); }
  catch(Exception e){ System.out.println("Invalid amount");

  }
  }else{
  System.out.println("Not found");

  }
 }else if(choice == 3){
 System.out.print("Account Number: ");
  Account acc = bank.findAccount(sc.nextLine());

  if(acc!= null){
  System.out.print("Amount: ");

 try{ double amt = Double.parseDouble(sc.nextLine()); acc.withdraw(amt); }
 catch(Exception e){
 System.out.println("Invalid amount");

 }
  }
 }else if(choice == 4){
 // bubble sort hard coded myself - Unit 1
 ArrayList<Account> list = bank.getAccounts();
 int n = list.size();
 for(int i =0; i < n -1; i++){
  for(int j =0; j < n - i -1; j++){
  if(list.get(j).getBalance() > list.get(j+1).getBalance()){
   Account temp = list.get(j);
   list.set(j, list.get(j+1));
  list.set(j+1, temp);
  }
  }
 }
 bank.displayAllAccounts();

 }else if(choice == 5){
  new BankGUI(bank);

 }else if(choice == 6){
  System.out.println("Bye!");

  break;
 }
 }
 sc.close();
}
}

enum AccountType {
 SAVINGS,
CHEQUE
}

abstract class Account {
 protected String accountNumber;
 protected String ownerName;
protected double balance;
 protected AccountType type;
 protected String[][] transactions;
protected int transactionCount;

public Account(String accountNumber, String ownerName, double balance, AccountType type){
  this.accountNumber = accountNumber;
 this.ownerName = ownerName;
  this.balance = balance;
 this.type = type;
  transactions = new String[100][2];
 transactionCount = 0;
}

 public abstract void calculateInterest();

public boolean deposit(double amount){
 if(amount <= 0){
  System.out.println("Amount must be positive");

 return false;
 }
 balance += amount;
 addTransaction("DEPOSIT", String.valueOf(amount));
 return true;
}

public boolean withdraw(double amount){
  if(amount > balance){
 System.out.println("Insufficient funds. Balance: R" + balance);

  return false;
 }
 balance -= amount;
  addTransaction("WITHDRAW", String.valueOf(amount));
 return true;
}

protected void addTransaction(String tType, String tAmount){
 if(transactionCount < transactions.length){
  transactions[transactionCount][0] = tType;
  transactions[transactionCount][1] = tAmount;
 transactionCount++;
 }
}

public double getBalance(){ return balance; }
 public String getAccountNumber(){ return accountNumber; }

@Override
public String toString(){
 return accountNumber + " | " + ownerName + " | " + type + " | R" + balance;
}
}

class SavingsAccount extends Account {
private double interestRate;

 public SavingsAccount(String accountNumber, String ownerName, double balance){
  super(accountNumber, ownerName, balance, AccountType.SAVINGS);
 this.interestRate = 0.05;
}

@Override
public void calculateInterest(){
 balance += balance * interestRate;
}
}

class ChequeAccount extends Account {
 public ChequeAccount(String accountNumber, String ownerName, double balance){
 super(accountNumber, ownerName, balance, AccountType.CHEQUE);
}

@Override
public void calculateInterest(){
 System.out.println("Cheque account has no interest");

}
}

class Bank {
 private ArrayList<Account> accounts;

public Bank(){
 accounts = new ArrayList<>();
}

public void addAccount(Account acc){
 accounts.add(acc);
}

public ArrayList<Account> getAccounts(){
 return accounts;
}

public Account findAccount(String accNumber){
 for(Account acc : accounts){
  if(acc.getAccountNumber().equals(accNumber)){
  return acc;
  }
 }
 return null;
}

public void displayAllAccounts(){
 for(Account acc : accounts){
  System.out.println(acc);

 for(int i=0; i < acc.transactionCount; i++){
  System.out.println(" " + acc.transactions[i][0] + " - R" + acc.transactions[i][1]);

 }
 }
}
}

class BankGUI extends JFrame {
public BankGUI(Bank bank){
 setTitle("Banking System");
 setSize(600,400);
 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 JTextArea area = new JTextArea(15,50);
 JButton btnDisplay = new JButton("Display");
 btnDisplay.addActionListener(e -> {
  area.setText("");
 for(Account a : bank.getAccounts()){
  area.append(a.toString()+"\n");
 }
 });
 JButton btnSort = new JButton("Sort by Balance");
 btnSort.addActionListener(e -> {
 ArrayList<Account> list = bank.getAccounts();
 int n = list.size();
 for(int i=0;i<n-1;i++){
  for(int j=0;j<n-i-1;j++){
  if(list.get(j).getBalance() > list.get(j+1).getBalance()){
  Account temp = list.get(j);
  list.set(j,list.get(j+1));
  list.set(j+1,temp);
  }
  }
 }
  area.setText("--- Sorted ---\n");
 for(Account a : bank.getAccounts()){
  area.append(a.toString()+"\n");
 }
 });
 JPanel p = new JPanel();
 p.add(btnDisplay);
 p.add(btnSort);
 p.add(new JScrollPane(area));
 add(p);
 setVisible(true);
}
}