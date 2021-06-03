package com.example.babyneedsapp.model;

public class BabyItem {

    private int id;
    private String babyItem;
    private int quantity;
    private String colour;
    private int size;
    private String dateItemAdded;

    public BabyItem(int id, String babyItem, int quantity, String colour, int size, String dateItemAdded) {
        this.id = id;
        this.babyItem = babyItem;
        this.quantity = quantity;
        this.colour = colour;
        this.size = size;
        this.dateItemAdded = dateItemAdded;
    }

    public BabyItem(String babyItem, int quantity, String colour, int size, String dateItemAdded) {
        this.babyItem = babyItem;
        this.quantity = quantity;
        this.colour = colour;
        this.size = size;
        this.dateItemAdded = dateItemAdded;
    }


    public BabyItem() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBabyItem() {
        return babyItem;
    }

    public void setBabyItem(String babyItem) {
        this.babyItem = babyItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }
}
