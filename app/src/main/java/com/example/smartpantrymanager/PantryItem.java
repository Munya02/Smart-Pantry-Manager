package com.example.smartpantrymanager;

public class PantryItem {

    // =========================================
    // VARIABLES
    // =========================================

    private int id;
    private String itemName;
    private double quantity;
    private String unit;


    // =========================================
    // CONSTRUCTOR
    // =========================================

    public PantryItem(
            int id,
            String itemName,
            double quantity,
            String unit
    ) {

        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unit = unit;
    }


    // =========================================
    // GET ID
    // =========================================

    public int getId() {
        return id;
    }


    // =========================================
    // GET ITEM NAME
    // =========================================

    public String getItemName() {
        return itemName;
    }


    // =========================================
    // GET QUANTITY
    // =========================================

    public double getQuantity() {
        return quantity;
    }


    // =========================================
    // GET UNIT
    // =========================================

    public String getUnit() {
        return unit;
    }
}