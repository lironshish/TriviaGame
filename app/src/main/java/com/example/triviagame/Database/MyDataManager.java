package com.example.triviagame.Database;

import com.google.firebase.storage.FirebaseStorage;

public class MyDataManager{
    private final FirebaseStorage storage;


    private static MyDataManager single_instance = null;

    private MyDataManager() {
        storage = FirebaseStorage.getInstance();
    }

    public static MyDataManager getInstance() {
        return single_instance;
    }

    public static MyDataManager initHelper() {
        if (single_instance == null) {
            single_instance = new MyDataManager();
        }
        return single_instance;
    }



}
