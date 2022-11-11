package com.example.triviagame.Objects;

public class Topic {

    private String name;
    private String image = "";

    //Default constructor
    public Topic(){

    }

    public Topic(String name, String image) {
        this.name = name;
        this.image = image;
    }

    //Setters and Getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
