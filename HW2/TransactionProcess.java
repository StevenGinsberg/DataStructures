package com.jetbrains;

import java.io.File;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class TransactionProcess
{
    private CSVModel transactions;
    private double[] itemPrices;
    private int[] quantities;
    private Warehouse[] warehouses;
    private NumberFormat USD;

    // constructor for an arbitrary number of columns.
    public TransactionProcess(File transFile) {
        transactions = new CSVModel(transFile);
        initializeGlobals();
    }
    // constructor for specifying the exact number of columns.
    public TransactionProcess(File transFile, int col) {
        transactions = new CSVModel(transFile, col);
        initializeGlobals();
    }

    private void initializeGlobals() {
        itemPrices = new double[3];
        quantities = new int[3];
        warehouses = new Warehouse[5];
        USD = NumberFormat.getCurrencyInstance(Locale.US);
        for(int i = 0 ; i < warehouses.length ; i++)
            warehouses[i] = new Warehouse(Location.City.values()[i]);
    }

    // Reads through transaction records ('O' or 'S').
    public void processTransactions() {
        parseItemPrices();
        // for each ArrayList<> element in the model.
        for (Object[] tr : transactions.getModel()) {
            // loop through the Object[] array contained in it.
            for (int i = 0 ; i < tr.length ; i++) {
                String record = Arrays.toString(tr);
                if (record.contains(warehouses[i].getLocation().toString())) {
                    if (tr[0].equals("S")) {
                        System.out.printf("%nShipment: %s%n", Arrays.toString(tr));
                        ship(tr, warehouses[i], null);
                    } else if (tr[0].equals("O")) {
                        System.out.printf("%nOrder: %s%n", Arrays.toString(tr));
                        order(tr, warehouses[i]);
                    }
                }
            }
        }
    }

    // Processes shipments to a given warehouse location.
    protected void ship(Object[] tr, Warehouse receivingWarehouse,
                        Location.City sendingLocation)
    {
        parseQuantities(tr);
        receivingWarehouse.receiveShipment(quantities);
        String notice = (sendingLocation == null ?
                "shipped" : "shipped from "+sendingLocation.toString());

        // print a notice of the quantities for each item shipped to a location.
        for (int i = 0, num = 1 ; i < 3 ; i++, num++) {
            System.out.printf("%d of item %d %s to %s%n",
                    quantities[i], num,
                    notice, receivingWarehouse.getLocation());
        }
        //print the adjusted stock of the receiving warehouse
        System.out.printf("Adjusted stock: %s %s%n",
                receivingWarehouse.getLocation(),
                Arrays.toString(receivingWarehouse.getStock()));
    }

    // Processes orders from a given warehouse location.
    protected void order(Object[] tr, Warehouse orderingWarehouse) {
        parseQuantities(tr);
        // loop through each warehouse to find an adequate stock of items for shipping.
        for (Warehouse w : warehouses) {
            if (orderingWarehouse.equals(w)) continue;
            if (w.getStock()[0] > quantities[0]
                    && w.getStock()[1] > quantities[1]
                    && w.getStock()[2] > quantities[2])
            {
                w.sendShipment(quantities);
                ship(tr, orderingWarehouse, w.getLocation());
                System.out.printf("Price of Order: %s%n",
                        USD.format(calculatePrice(quantities)));
                return;
            }
        }
        System.out.println("Order unfilled.");
    }

    // Stores the initial item prices from the first line of the model for reference.
    private void parseItemPrices() {
        try {
            // Loop through the array contained in the first element of the ArrayList
            // for the prices.
            for (int i = 0 ; i < transactions.getModel().get(0).length ; i++) {
                itemPrices[i] = Double.parseDouble(
                        transactions.getModel().get(0)[i].toString());
            }
        } catch (NumberFormatException nfe) {
            System.out.println(nfe.getMessage());
        }
        // remove first line of the CSV model to avoid data mismatch concerns.
        transactions.getModel().remove(0);
    }

    // Stores item quantities for reference.
    private void parseQuantities(Object[] tr) {
        for (int i = 0, j = 2 ; i < quantities.length ; i++, j++)
            quantities[i] = Integer.parseInt(tr[j].toString());
    }

    protected double calculatePrice(int[] quantities) {
        double prices[] = new double[3];
        for (int i = 0 ; i < 3 ; i++)
            prices[i] = itemPrices[i] * quantities[i];
        // duck screw lawn mow
        double price = 0.00;
        for (int i = 0 ; i < 3 ; i++)
            price += prices[i];

        return (price + (price * 10)/100);
    }

    public Warehouse[] getWarehouses() {
        return warehouses;
    }
}
