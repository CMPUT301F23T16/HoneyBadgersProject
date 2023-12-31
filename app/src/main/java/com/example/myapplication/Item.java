package com.example.myapplication;

import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * This class represents an item.
 */

public class  Item implements Serializable {
    private String name;
    private double price;
    private String dateAdded;
    private String description;
    private String make;
    private String model;
    private String serial;
    private String comment;
    private List<String> tag;
    private String id;
    private boolean isSelected = false;
    private List<String> image_refs;


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

    public List<String> getTag() {
        return tag;
    }

    public void setTag(String tag) {
        if (this.tag == null) {
            this.tag = new ArrayList<>();
        }
        this.tag.add(tag);
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
                String model, String serial, String comment, List<String> tag,List<String> image_references) {

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
        this.image_refs = image_references;
    }
    public Item(String name, double price, String dateAdded, String description, String make,
                String model, String serial, String comment, List<String> tag, List<String> image_references) {



        this.name = name;
        this.price = price;
        this.dateAdded = dateAdded;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serial = serial;
        this.comment = comment;
        this.tag = tag;
        this.image_refs = image_references;
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

    //constructor required for Firestore
    public Item(){}

    /**
     * getter for isSelected to verify if the checkbox of an item is selected or not
     * @return The value of isSelected which can be either true or false
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * setter for isSelected
     * @param selected
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    /**
     * This constructor is used when item(s) has to be deleted
     * @param name This is the name of item
     */
    public Item(String name){

        this.name = name;
        this.isSelected = isSelected();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter method for Image References
     * @return a list of strings for image references
     */
    public List<String> getImageRefs() {
        return image_refs;
    }

    /**
     * Setter method for Image References
     * @param imageRefs List of STrings for image references
     */
    public void setImageRefs(List<String> imageRefs) {
        this.image_refs = imageRefs;
    }
}