package com.jetbrains;

import java.util.Locale;
import java.util.regex.*;

public class Customer
{
    private int id;
    private String fullName;
    private String firstName, lastName;
    private double balance;

    public Customer(String name, int id,
                    double balance)
    {
        Pattern p = Pattern.compile("[\\s, \\t]");
        String[] firstLast = name.split(p.toString(), 2);
        this.firstName = firstLast[0].replaceAll(p.toString(), "");
        this.lastName = firstLast[1].replaceAll(p.toString(), "");
        this.fullName = firstName + " " + lastName;
        this.balance = balance;
        this.id = id;
    }

    public void printInfo() {
        java.text.NumberFormat format =
                java.text.NumberFormat.getCurrencyInstance(Locale.US);
        System.out.printf("%d%20s%15s%n", getId(),
                getFullName(), format.format(getBalance()));
    }

    public int getId() {
        return id;
    }
    public double getBalance() {
        return balance;
    }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() {
        return fullName;
    }
    protected void setBalance(double balance) {
        this.balance = balance;
    }
}
