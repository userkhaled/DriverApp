package com.eeducationgo.markoupdrivers.util;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class UberDriver extends MultiDexApplication {
    public static UberDriver instance;
    private static Boolean isChatActivityOpen = false;
    public static UberDriver getInstance() {
        return instance;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public static Boolean getIsChatActivityOpen() {
        return isChatActivityOpen;
    }

    public static void setIsChatActivityOpen(Boolean isChatActivityOpen) {
        UberDriver.isChatActivityOpen = isChatActivityOpen;
    }
}
