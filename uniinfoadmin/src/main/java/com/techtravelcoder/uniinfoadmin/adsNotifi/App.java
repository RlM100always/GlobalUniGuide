package com.techtravelcoder.uniinfoadmin.adsNotifi;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;


public class App extends Application {
    //37742494-b237-4ef9-9061-db4b6a76d8eb



    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // OneSignal Initialization


    }
}
