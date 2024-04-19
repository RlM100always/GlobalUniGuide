package com.techtravelcoder.alluniversityinformations.ads;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import co.notix.interstitial.InterstitialLoader;
import co.notix.interstitial.NotixInterstitial;

public class App extends Application {
    public static InterstitialLoader interstitialLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        /* ... */
        //load data offline using firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        interstitialLoader = NotixInterstitial.Companion.createLoader(7099848);
        interstitialLoader.startLoading();
    }
}
