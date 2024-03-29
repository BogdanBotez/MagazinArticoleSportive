package com.example.magazinarticolesportive.models;

public class Products {
    private String category;
    private String date;
    private String description;
    private String image;
    private String name;
    private String pid;
    private String size;
    private String time;
    private String gender;
    private String sport;
    private double price;
    private int quantity;

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public Products(String category, String date, String description, String image, String name, String pid, String size, double price, int quantity, String time, String gender, String sport) {
        this.category = category;
        this.date = date;
        this.description = description;
        this.image = image;
        this.name = name;
        this.pid = pid;
        this.size = size;
        this.time = time;
        this.gender = gender;
        this.sport = sport;
        this.price = price;
        this.quantity = quantity;
    }

    public Products(int quantity, double price, String name, String pid) {

        this.name = name;
        this.pid = pid;
        this.price = price;
        this.quantity = quantity;
    }

    public Products(){

    }

    public Products(String category, String name, String pid, String sport, double price) {
        this.category = category;
        this.name = name;
        this.pid = pid;
        this.sport = sport;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
