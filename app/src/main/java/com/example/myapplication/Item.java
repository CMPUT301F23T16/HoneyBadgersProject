package com.example.myapplication;

import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


/**
 * This class represents an item.
 */

public class Item implements Serializable {
    private String name;
    private double price;
    private String dateAdded;
    private String description;
    private String make;
    private String model;
    private String serial;
    private String comment;
    private String tag;

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDateAdded(Date dateAdded) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(new Date());

        this.dateAdded = strDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    /**
     * This constructor creates a new Item
     * @param name This is the name of the item
     * @param price This is the price of the item
     * @param dateAdded This is the date the item was added to the database
     */

    public Item(String name, double price, Date dateAdded) {
        this.name = name;
        this.price = price;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(new Date());

        this.dateAdded = strDate;
    }

    /**
     * This constructor creates a new Item using today's date
     * @param name This is the name of the item
     * @param price This is the price of the item
     */
    public Item(String name, double price) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String strDate = dateFormat.format(new Date());

        this.name = name;
        this.price = price;
        this.dateAdded = strDate;
    }

    /**
     * This constructor creates a new Item using all fields in Add Item Fragment
     */
    public Item(String name, double price, Date dateAdded, String description, String make,
                String model, String serial, String comment, String tag) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(dateAdded);

        this.name = name;
        this.price = price;
        this.dateAdded = strDate;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serial = serial;
        this.comment = comment;
        this.tag = tag;
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

    public String getDateAdded() {

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
