package com.jetbrains;

public class SalesRecord extends Node {
    private int quantitySold;

    // Constructor
    public SalesRecord(int quantitySold) {
        setQuantitySold(quantitySold);
        super.setType('S');
    }

    // quantitySold Setter:
    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    // quantitySold Getter:
    public int getQuantitySold() {
        return this.quantitySold;
    }

    // print Method:
    public void print() {
        System.out.println(super.getType()+ "\t" + quantitySold);
    }
}
