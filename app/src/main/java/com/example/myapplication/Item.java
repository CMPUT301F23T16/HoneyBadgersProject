package com.example.myapplication;

import java.time.LocalDate;

/**
 * This class represents an item.
 */
public class Item {
    private String name;
    private double price;
    private LocalDate dateAdded;

    /**
     * This constructor creates a new Item
     * @param name This is the name of the item
     * @param price This is the price of the item
     * @param dateAdded This is the date the item was added to the database
     */
    public Item(String name, double price, LocalDate dateAdded) {
        this.name = name;
        this.price = price;
        this.dateAdded = dateAdded;
    }

    /**
     * This constructor creates a new Item using today's date
     * @param name This is the name of the item
     * @param price This is the price of the item
     */
    public Item(String name, double price) {
        this.name = name;
        this.price = price;
        this.dateAdded = LocalDate.now();
    }

    /**
     * getter for name
     * @return The name of the item
     */
    public String getName() {
        return this.name;
    }

    /**
     * getter for date added
     * @return The addition date of the item
     */
    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    /**
     * getter for price
     * @return The price of the item
     */
    public Double getPrice() {
        return this.price;
    }
}
