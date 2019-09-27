package com.jetbrains;


public class Warehouse implements Location {
    private int[] items;
    private Location.City location;

    public Warehouse(Location.City loc) {
        this.items = new int[3];
        this.location = loc;
    }

    // Increases current items in stock.
    public void receiveShipment(int[] quantities) {
        for (int i = 0 ; i < 3 ; i++)
            items[i] += quantities[i];
    }

    // Decreases current items in stock.
    public void sendShipment(int[] quantities) {
        for (int i = 0 ; i < 3 ; i++)
            items[i] -= quantities[i];
    }

    public Location.City getLocation() {
        return location;
    }

    public int[] getStock() {
        return items;
    }
}