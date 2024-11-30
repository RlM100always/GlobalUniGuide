package com.techtravelcoder.alluniversityinformations.ads;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;

import java.util.Random;

public class ADSSetUp {

    private static InterstitialAd interstitialAd;


    public static void adsType1(Context context){

        interstitialAd = new InterstitialAd(context, "433943333055829_433946006388895");


        FirebaseDatabase.getInstance().getReference("Ads Control").child("faStatus")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String ans= (String) snapshot.getValue();
                    if(ans.equals("on")){
                        Random random=new Random();
                        int num = random.nextInt(10);
                        if(num==5){
                            InterstitialAdListener interstitialAdListener=new InterstitialAdListener() {
                                @Override
                                public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onInterstitialDismissed(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onError(com.facebook.ads.Ad ad, AdError adError) {

                                }

                                @Override
                                public void onAdLoaded(com.facebook.ads.Ad ad) {

                                    interstitialAd.show();

                                }

                                @Override
                                public void onAdClicked(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onLoggingImpression(com.facebook.ads.Ad ad) {

                                }
                            };
                            interstitialAd.loadAd(
                                    interstitialAd.buildLoadAdConfig()
                                            .withAdListener(interstitialAdListener)
                                            .build());

                        }


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}
