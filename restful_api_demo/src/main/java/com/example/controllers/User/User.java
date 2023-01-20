package com.example.controllers.User;

public class User {
    private String name = "";
    private String email = "";

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(){
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}