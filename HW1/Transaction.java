package com.jetbrains;
public class Transaction {
    protected String itemName;
    protected double price;
    protected int quantity;
    protected double paymentAmnt;
    protected enum Type { ORDER, PAYMENT }
    private Type type;

    public Transaction() { }
    public Transaction(String name, double price,
                       int quantity)
    {
        this.type = Type.ORDER;
        this.itemName = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Transaction(double paymentAmnt) {
        this.type = Type.PAYMENT;
        this.paymentAmnt = paymentAmnt;
    }

    public double getPrice() { return price; }
    public String getName() { return itemName; }
    public int getQuantity() { return quantity; }
    public Type getType() { return type; }
    public double getPaymentAmnt() { return paymentAmnt; }
}
