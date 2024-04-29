package com.techtravelcoder.alluniversityinformations.ads;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.startapp.sdk.adsbase.StartAppAd;

import co.notix.appopen.AppOpenLoader;
import co.notix.appopen.NotixAppOpen;
import co.notix.interstitial.InterstitialLoader;
import co.notix.interstitial.NotixInterstitial;

public class App extends Application {
    public static InterstitialLoader interstitialLoader;
    public static AppOpenLoader appOpenLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        StartAppAd.disableSplash();

        /* ... */
        //load data offline using firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        interstitialLoader = NotixInterstitial.Companion.createLoader(7099848);
        interstitialLoader.startLoading();

//        appOpenLoader = NotixAppOpen.Companion.createLoader(7385969);
//        appOpenLoader.startLoading();
//        NotixAppOpen.Companion.startAutoShow(App.appOpenLoader);
    }
}
