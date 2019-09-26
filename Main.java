package com.jetbrains;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

/*
 * Author: LEIBNIZ H. BERIHUETE
 * Course: CISC 3130-W6
 * Last Modification: 05/12/2016
 * Homework #3
 * NOTE: THIS CLASS IS THE MAIN CLASS
 */

public class Main {
    //--------------------- Data Field ----------------------//
    private static File file;                            //
    private static Scanner scanner;                      //
    private static final double MARK_UP_CHARGE = 0.30;   //
    private static int discountCount = 0;                //
    private static double discount = 0.0;                //
    private static Node first = null;                    //
    private static Node p = null;                        //
//-------------------------------------------------------//

    /* *******************
     * 	  MAIN METHOD
     * ******************/
    public static void main (String [] args) {
        // Have the file Ready:
        try{
            file = new File("Datathree.txt");
            scanner = new Scanner(file);
        }
        catch(IOException e) {
            // Print Error if file can't be opened
            System.out.println("ERROR: " + e.toString());
        }




        // Create Linked List
        createList();

        // Start selling
        startSelling();

        // Print remaining stocks
        printStocksLeft();

    } // END OF MAIN


    /* *********************
     * 	 createList Method
     * ********************/
    private static void createList() {
        // In case it is the first line being read
        boolean firstLine = true;

        // While the file still have lines to be read.
        while(scanner.hasNext()) {
            String line = scanner.nextLine();

            // If this is the first line
            if(firstLine) {

                //---------------- WHEN READING FIRST LINE -----------------------//
                // If line is RECEIPT RECORD
                if(line.charAt(0) == 'R') {
                    int quantity = Integer.parseInt(line.substring(6,9));
                    double price = Double.parseDouble(line.substring(14));
                    first = p = new ReceiptRecord(quantity, price);
                    firstLine = false;

                }
                // If line is SALES RECORD
                else if(line.charAt(0) == 'S') {
                    System.out.println("There is nothing to sell at this time");
                    firstLine = true; // We want to keep it true,
                    // in case we reach another sale record
                    // without anything to sell.

                }

                // If line is PROMOTION CARD
                else if(line.charAt(0) == 'P') {
                    int discountFromData = Integer.parseInt(line.substring(6,9));
                    first = p = new PromotionCard(discountFromData);
                    firstLine = false;

                }
                else {
                    System.out.println("NO VALID RECORD ON THIS LINE");
                    firstLine = true;
                }
                //--------------- END OF IF STATEMENT--------------------------------//

            }
            // IF IT IS NOT THE FIRST LINE
            else {
                // If line is RECEIPT RECORD
                if(line.charAt(0) == 'R') {
                    int quantity = Integer.parseInt(line.substring(6,9));
                    double price = Double.parseDouble(line.substring(14));
                    p.next = new ReceiptRecord(quantity, price);
                    p = p.next;



                }
                // If line is SALES RECORD
                else if(line.charAt(0) == 'S') {
                    int quantity = Integer.parseInt(line.substring(6,9));
                    p.next = new SalesRecord(quantity);
                    p = p.next;


                }
                // If line is PROMOTION CARD
                else if(line.charAt(0) == 'P') {
                    int discountFromData = Integer.parseInt(line.substring(6,9));
                    p.next = new PromotionCard(discountFromData);
                    p = p.next;

                }
                else {
                    System.out.println("Incorrect data.");
                }

            }// END OF ELSE STATEMENT
        } // END OF WHILE LOOP (list created)
    }


    /* **********************
     * 	 startSelling Method
     * *********************/
    private static void startSelling() {
        // Create a money format
        DecimalFormat money = new DecimalFormat("$0.00");

        // Lets go through the list;
        p = first;
        while(p != null) {
            // IF -- R
            if(p.getType() == 'R') {
                ((ReceiptRecord)p).print();
                p = p.next;
            }

            // IF -- P
            else if(p.getType() == 'P') {
                ((PromotionCard)p).print();
                discountCount += 2;
                discount = (double)((PromotionCard)p).getDiscount()/100.0;
                p = p.next;
            }

            // IF -- S
            else if(p.getType() == 'S') {
                double total = 0;
                ((SalesRecord)p).print();
                SalesRecord s = ((SalesRecord)p);
                int currentQuantityToSell = s.getQuantitySold();
                String transactions = "";

                // Let's find receipt records until sales quantity equal zero
                //  or to sell as much as there is available.
                while(s.getQuantitySold()!=0) {
                    if(first.getType() == 'R') {
                        ReceiptRecord r = ((ReceiptRecord)first);
                        int widgetsSold = s.getQuantitySold();
                        int currentQuantity =r.getQuantity();
                        double price = r.getPrice() + (r.getPrice()*MARK_UP_CHARGE);
                        total+=currentQuantity*price;

                        // When Sale is larger than Receipt
                        if(widgetsSold > currentQuantity) {
                            transactions +="\t" + currentQuantity + " at " +
                                    money.format((price)) + "\tSales: " +
                                    money.format(currentQuantity*price)+"\n";

                            s.setQuantitySold(widgetsSold-currentQuantity);
                            r.setQuantity(0);
                            first = first.next;
                        }

                        // When Sale is less than or equal to receipt
                        else if (widgetsSold <= currentQuantity) {
                            transactions += "\t" + widgetsSold +
                                    " at " + money.format((price)) + "\tSales: " +
                                    money.format(currentQuantity*price)+"\n";

                            s.setQuantitySold(0);
                            r.setQuantity(currentQuantity-widgetsSold);
                            if(r.getQuantity()==0) {
                                first = first.next;
                            }
                        }
                    }

                    // if we don't find any receipt to be able to sell more
                    else if(first.equals(s)) {
                        transactions += "remainder of "+ s.getQuantitySold() +
                                " Widgets not available"+"\n";
                        break;
                    }
                    else {
                        first = first.next;
                    }
                }

                // If we have any discount
                if(discountCount > 0) {
                    transactions += "\t\t\tTotal: " + money.format(total)+ "\n";
                    transactions += "\t\t\tDiscount: " + (int)(discount*100)+ "%\n";
                    total -= (total*discount);
                    discountCount--;
                }
                // print transactions:
                int sold = currentQuantityToSell - s.getQuantitySold();
                System.out.println("****************************************");
                System.out.println("\t"+sold+ " Widgets Sold");
                System.out.println(transactions);
                System.out.println("\t\t  Total Sales: " + money.format(total));
                System.out.println("----------------------------------------\n\n");
                p = p.next;
            }
        }// END of while loop
    } // END OF startSelling Method

    /* **************************
     * 	 printStocksLeft Method.
     * **************************/
    private static void printStocksLeft() {
        p = first;
        System.out.println("%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("     STOCKS LEFT     ");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%");

        while(p!=null) {
            if(p.getType() == 'R') {
                // Print Receipt Record
                ((ReceiptRecord)p).print();
            }
            p = p.next;
        }
    }
}