package com.example.magazinarticolesportive.models;

public class Wish {

    String name, sport, category, pId;
    double price;

    public String getName() {
        return name;
    }

    public Wish() {
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public Wish(String category, String name, String pId, double price, String sport) {
        this.pId = pId;
        this.name = name;
        this.sport = sport;
        this.category = category;
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}