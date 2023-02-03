package com.example.triviagame.Database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class MyDataManager{
    private final FirebaseStorage storage;
    private final FirebaseDatabase realTimeDB;
    private final FirebaseAuth firebaseAuth;


    private static MyDataManager single_instance = null;

    private MyDataManager() {
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        realTimeDB = FirebaseDatabase.getInstance();
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

    public FirebaseStorage getStorage() {
        return storage;
    }

    public FirebaseDatabase getRealTimeDB() {
        return realTimeDB;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public static MyDataManager getSingle_instance() {
        return single_instance;
    }
}
