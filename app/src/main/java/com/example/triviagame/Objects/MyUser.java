package com.example.triviagame.Objects;

public class MyUser {

    private String name;
    private String email;
    private String password;
    private String UID;
    private boolean premium;

    public MyUser(String name, String email, String password, boolean premium) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.premium = premium;
    }

    public MyUser() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }
}
