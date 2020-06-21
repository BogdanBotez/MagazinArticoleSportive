package com.example.magazinarticolesportive.models;

public class Coupons {

    private String name, value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Coupons() {
    }

    public Coupons(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
