package com.example.magazinarticolesportive.Model;

public class Orders {
    private String address, phone, name, totalPrice, date, time;

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Orders(String address, String phone, String name, String totalPrice, String date, String time) {
        this.address = address;
        this.phone = phone;
        this.name = name;
        this.totalPrice = totalPrice;
        this.date = date;
        this.time = time;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Orders() {
    }
}
