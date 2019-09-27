package com.jetbrains;

import java.text.NumberFormat;
import java.util.*;

public class Record
{
    private Customer customer;
    private double balanceDue;
    private NumberFormat currencyFormat;
    private List<Transaction> transactions = new ArrayList<>();

    public Record() { }
    public Record(Customer customer) {
        currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        this.customer = customer;
    }

    public void addOrder(String itemName, int quantity,
                         double price)
    {
        transactions.add(new Transaction(
                itemName, price, quantity));
    }

    public void addPayment(double paymentAmount) {
        transactions.add(new Transaction(paymentAmount));
    }

    protected void calculateBalance() {
        balanceDue = 0;
        for(Transaction trans : transactions) {
            if (trans.getType().equals(Transaction.Type.ORDER)) {
                balanceDue += (trans.getPrice() * trans.getQuantity());
            }
            else if (trans.getType().equals(Transaction.Type.PAYMENT)) {
                balanceDue -= trans.getPaymentAmnt();
            }
        }
        customer.setBalance(balanceDue);
    }

    public void printTransactions() {
        int transNumber = 0;
        System.out.println("\nTRANSACTIONS");
        System.out.printf("%6s%9s%27s%11s%12s%n",
                "#", "TYPE", "ITEM NAME", "QUANTITY", "AMOUNT");
        for(Transaction t : transactions) {
            transNumber++;
            if(t.getType().equals(Transaction.Type.ORDER)) {
                System.out.printf("%6d%9s%27s%11d%12s%n",
                        transNumber, t.getType(), t.getName(),
                        t.getQuantity(), currencyFormat.format(t.getPrice()));
            } else {
                System.out.printf("%6d%9s%27s%11s%12s%n",
                        transNumber, t.getType(), "-", "-",
                        currencyFormat.format(t.getPaymentAmnt()));
            }
        }
    }

    public void printInvoice() {
        System.out.println("\n---INVOICE------");
        System.out.printf("%-21s%21s%21s",
                "CUSTOMER NAME", "CUSTOMER ID", "PAST BALANCE");
        System.out.printf("%n%-21s%21s%21s%n",
                customer.getFullName(), customer.getId(),
                currencyFormat.format(customer.getBalance()));

        printTransactions();
        calculateBalance();
        System.out.printf("%n%55s%10s%n", "BALANCE DUE: ",
                currencyFormat.format(balanceDue));
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
