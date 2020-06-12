package com.example.magazinarticolesportive.Model;

public class Wish {

    String name, sport, category, pId;
    double price;

    public String getName() {
        return name;
    }

    public Wish() {
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public Wish(String pId, String name, double price, String category,  String sport) {
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
