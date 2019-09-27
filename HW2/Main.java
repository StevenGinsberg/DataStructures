package com.jetbrains;


import java.io.File;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a transaction file path: ");
        String inputPath = scan.nextLine();
        TransactionProcess record =
                new TransactionProcess(new File(inputPath));
        record.processTransactions();
    }
}