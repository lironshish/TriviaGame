package com.example.triviagame;

import android.app.Application;

import com.example.triviagame.Database.MyDataManager;
import com.google.android.gms.ads.MobileAds;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //MobileAds.initialize(this);
        //Initiate FireBase Managers
        MyDataManager.initHelper();
    }
}
