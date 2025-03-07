package com.techtravelcoder.alluniversityinformations.ads;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    public static void adsType1(Context context){

        FirebaseDatabase.getInstance().getReference("Ads Control").child("faStatus")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String ans= (String) snapshot.getValue();
                    if(ans.equals("on")){
                        Random random=new Random();
                        int num = random.nextInt(5);
                        if(num==1){
                            final StartAppAd rewardedVideo = new StartAppAd(context);

                            rewardedVideo.setVideoListener(new VideoListener() {
                                @Override
                                public void onVideoCompleted() {
                                    // Grant the reward to user
                                }
                            });

                            rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                                @Override
                                public void onReceiveAd(Ad ad) {
                                    rewardedVideo.showAd();
                                }

                                @Override
                                public void onFailedToReceiveAd(Ad ad) {
                                    // Can't show rewarded video
                                }
                            });
                        }
                        if(num==0){
                            StartAppAd interstitialAd = new StartAppAd(context);
                            interstitialAd.loadAd(new AdEventListener() {
                                @Override
                                public void onReceiveAd (@NonNull Ad ad){
                                    // TODO save interstitialAd somewehre, then call interstitialAd.showAd()
                                    interstitialAd.showAd();

                                }

                                @Override
                                public void onFailedToReceiveAd (@Nullable Ad ad){
                                    Toast.makeText(context, "miss", Toast.LENGTH_SHORT).show();
                                }
                            });
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
