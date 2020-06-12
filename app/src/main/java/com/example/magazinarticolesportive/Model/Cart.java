package com.example.magazinarticolesportive.Model;

public class Cart {

    private String pid, name, discount;
    private double price;
    private int quantity;

    public Cart(){

    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
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

    public Cart(String pid, String name, String discount, double price, int quantity) {
        this.pid = pid;
        this.name = name;
        this.discount = discount;
        this.price = price;
        this.quantity = quantity;
    }
}
