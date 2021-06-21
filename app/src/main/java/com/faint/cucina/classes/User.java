package com.faint.cucina.classes;

public class User {
    private int id;
    private String name, phone, city;

    public User(int id, String name, String phone, String city) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String gender) {
        this.phone = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
