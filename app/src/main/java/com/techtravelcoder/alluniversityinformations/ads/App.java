package com.techtravelcoder.alluniversityinformations.ads;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.onesignal.notifications.INotificationClickEvent;
import com.onesignal.notifications.INotificationClickListener;
import com.startapp.sdk.adsbase.StartAppAd;
import com.techtravelcoder.alluniversityinformations.countryDetails.MainActivity;
import com.techtravelcoder.alluniversityinformations.postDetails.PostHandleActivity;
import com.techtravelcoder.alluniversityinformations.universityDetails.ReBookMarkActivity;
import com.techtravelcoder.alluniversityinformations.vocabulary.VocabularyActivity;

import co.notix.appopen.AppOpenLoader;
import co.notix.appopen.NotixAppOpen;
import co.notix.interstitial.InterstitialLoader;
import co.notix.interstitial.NotixInterstitial;

public class App extends Application {
    public static InterstitialLoader interstitialLoader;
    public static AppOpenLoader appOpenLoader;
    private static final String ONESIGNAL_APP_ID = "4966cbfa-9bdd-4424-9b60-b11fd884cee5";
    //e372314f-1668-4787-80ee-52f143fdc1fa
    //private static final String ONESIGNAL_APP_ID1 ="e372314f-1668-4787-80ee-52f143fdc1fa";



    @Override
    public void onCreate() {
        super.onCreate();
        StartAppAd.disableSplash();

        /* ... */
        //load data offline using firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        interstitialLoader = NotixInterstitial.Companion.createLoader(7099848);
        interstitialLoader.startLoading();

        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);

        OneSignal.getNotifications().addClickListener(new INotificationClickListener() {
            @Override
            public void onClick(@NonNull INotificationClickEvent iNotificationClickEvent) {
                iNotificationClickEvent.getNotification().getTemplateName();
                Toast.makeText(getApplicationContext(), ""+iNotificationClickEvent.getNotification().getTemplateName(), Toast.LENGTH_SHORT).show();
                //Log.d("Value",iNotificationClickEvent.getNotification().getTemplateName());
                if(iNotificationClickEvent.getNotification().getTemplateName().equals("University")){
                    Intent intent=new Intent(getApplicationContext(), ReBookMarkActivity.class);
                    appOpenLoader = NotixAppOpen.Companion.createLoader(7385969);
                    appOpenLoader.startLoading();
                    NotixAppOpen.Companion.startAutoShow(appOpenLoader);
                    intent.putExtra("check",1);
                    startActivity(intent);
                }
                if(iNotificationClickEvent.getNotification().getTemplateName().equals("Popular Content")){
                    appOpenLoader = NotixAppOpen.Companion.createLoader(7385969);
                    appOpenLoader.startLoading();
                    NotixAppOpen.Companion.startAutoShow(App.appOpenLoader);
                    Intent intent=new Intent(getApplicationContext(), ReBookMarkActivity.class);
                    intent.putExtra("check",2);

                    startActivity(intent);
                }
                if(iNotificationClickEvent.getNotification().getTemplateName().equals("Dictionary")){
                    appOpenLoader = NotixAppOpen.Companion.createLoader(7385969);
                    appOpenLoader.startLoading();
                    NotixAppOpen.Companion.startAutoShow(App.appOpenLoader);
                    Intent intent=new Intent(getApplicationContext(), VocabularyActivity.class);

                    startActivity(intent);
                }
                if(iNotificationClickEvent.getNotification().getTemplateName().equals("Post")){
                    appOpenLoader = NotixAppOpen.Companion.createLoader(7385969);
                    appOpenLoader.startLoading();
                    NotixAppOpen.Companion.startAutoShow(App.appOpenLoader);
                    Intent intent=new Intent(getApplicationContext(), PostHandleActivity.class);

                    startActivity(intent);
                }



            }
        });


//        appOpenLoader = NotixAppOpen.Companion.createLoader(7385969);
//        appOpenLoader.startLoading();
//        NotixAppOpen.Companion.startAutoShow(App.appOpenLoader);
    }
}
