package com.techtravelcoder.alluniversityinformations.ads;

import android.content.Context;

import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;

import java.util.Random;

import co.notix.interstitial.NotixInterstitial;
import kotlin.Unit;

public class ADSSetUp {

    public static void adsType1(Context context){
        Random random=new Random();
        int num = random.nextInt(3);
        if(num==0){
            // FacebookInterestitialAds.loadAds(context);
            App.interstitialLoader.doOnNextAvailable(result -> {
                if (result != null) {
                    NotixInterstitial.Companion.show(result);
                }
                return Unit.INSTANCE;
            });

        }
        if(num==1){
            App.interstitialLoader.doOnNextAvailable(result -> {
                if (result != null) {
                    NotixInterstitial.Companion.show(result);
                }
                return Unit.INSTANCE;
            });

        }
        if(num==2){
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

    }

    public static void adsType2(){

    }


    public static void adsType3(){

    }
}
