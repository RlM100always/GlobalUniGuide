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
import com.techtravelcoder.alluniversityinformations.books.BookPostActivity;
import com.techtravelcoder.alluniversityinformations.postDetails.PostHandleActivity;
import com.techtravelcoder.alluniversityinformations.universityDetails.ReBookMarkActivity;
import com.techtravelcoder.alluniversityinformations.vocabulary.VocabularyActivity;

public class App extends Application {

   // private static final String ONESIGNAL_APP_ID = "4966cbfa-9bdd-4424-9b60-b11fd884cee5";
    //e372314f-1668-4787-80ee-52f143fdc1fa
    private static final String ONESIGNAL_APP_ID ="e372314f-1668-4787-80ee-52f143fdc1fa";
    private static final int REQUEST_USE_FULL_SCREEN_INTENT = 1;




    @Override
    public void onCreate() {
        super.onCreate();
        StartAppAd.disableSplash();

        /* ... */
        //load data offline using firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);



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
                    intent.putExtra("check",1);
                    startActivity(intent);
                }
                if(iNotificationClickEvent.getNotification().getTemplateName().equals("Popular Content")){
                    Intent intent=new Intent(getApplicationContext(), ReBookMarkActivity.class);
                    intent.putExtra("check",2);

                    startActivity(intent);
                }
                if(iNotificationClickEvent.getNotification().getTemplateName().equals("Dictionary")){
                    Intent intent=new Intent(getApplicationContext(), VocabularyActivity.class);
                    startActivity(intent);
                }
                if(iNotificationClickEvent.getNotification().getTemplateName().equals("Post")){
                    Intent intent=new Intent(getApplicationContext(), PostHandleActivity.class);
                    startActivity(intent);
                }
                if(iNotificationClickEvent.getNotification().getTemplateName().equals("Books")){
                    Intent intent=new Intent(getApplicationContext(), BookPostActivity.class);
                    intent.putExtra("key","@b");
                    startActivity(intent);
                }




            }
        });


    }
}
