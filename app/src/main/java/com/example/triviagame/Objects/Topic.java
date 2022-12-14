package com.example.triviagame.Objects;

public class Topic {

    private String title;
    private String image = "https://firebasestorage.googleapis.com/v0/b/triviagame-87e3b.appspot.com/o/Topics%2Fic_question.jpeg?alt=media&token=b0b9f4a1-524e-4d8e-bbf2-f8e3185ccefc";
    private String webPage = "";
    private boolean premium = false;


    //Default constructor
    public Topic(){

    }

    public Topic(String name, String image, String webPage, boolean premium) {
        this.title = name;
        this.image = image;
        this.webPage = webPage;
        this.premium = premium;
    }

    //Setters and Getters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }
}
