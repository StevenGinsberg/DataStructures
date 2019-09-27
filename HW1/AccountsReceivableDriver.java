package com.jetbrains;
 
import java.io.File;
import java.util.*;

public class AccountsReceivableDriver
{
    public static AccountsReceivable accountsIndex;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a master path: ");
        String master = scan.nextLine();
        System.out.println("Enter a records path: ");
        String record = scan.nextLine();
        accountsIndex = new AccountsReceivable(
                new File(master), new File(record));
        System.out.println(accountsIndex.getAccounts().size() +
                " customers available.");

        provideOptions();
    }

    public static void provideOptions() {
        int option;
        do {
            System.out.print("\n1. Print All Customers\n" +
                    "2. Print All Invoices\n" +
                    "3. Search Customers\n" +
                    "4. Exit\n" +
                    "Enter an option (1-4): ");
            Scanner scanOpt = new Scanner(System.in);
            option = scanOpt.nextInt();

            switch (option) {
                case 1 : printCustomers(); break;
                case 2 : printInvoices();  break;
                case 3 : searchCustomer(); break;
                default: if(option == 4)   break;
                    else System.out.println("Incorrect option.\n");
            }
        } while(option != 4);
    }

    public static void printCustomers() {
        System.out.printf("%s%22s%15s%n", "ID", "NAME", "BALANCE");
        for(Map.Entry<Customer, Record> cr :
                accountsIndex.getAccounts().entrySet()) {
            cr.getKey().printInfo();
        }
    }

    public static void printInvoices() {
        System.out.printf("Printing invoices for %d customers%n",
                accountsIndex.getAccounts().size());
        for(Record rec : accountsIndex.getAccounts().values())
            rec.printInvoice();
    }

    public static void searchCustomer() {
        Scanner scan = new Scanner(System.in);
        String name = "";

        int id = 0;
        System.out.print("Enter a customer's ID or full name to search: ");
        String searchKey = scan.nextLine();
        try {
            name = null;
            id = Integer.parseInt(searchKey);
        } catch (NumberFormatException nfe) {
            name = searchKey;
        } finally {
            for (Map.Entry<Customer, Record> cr : accountsIndex.getAccounts().entrySet()) {
                if (cr.getKey().getFullName().equalsIgnoreCase(name) ||
                        cr.getKey().getId() == id)
                {
                    System.out.println("Customer found");
                    cr.getValue().printInvoice();
                    break;
                }
            }
        }
    }
}
