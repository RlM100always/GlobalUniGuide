package com.techtravelcoder.alluniversityinformations.ads;

import android.content.Context;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.techtravelcoder.alluniversityinformation.R;


public class FacebookInterestitialAds {

    public static InterstitialAd interstitialAd;

    public static void loadAds(Context context){

        AudienceNetworkAds.initialize(context);
        interstitialAd=new InterstitialAd(context,context.getString(R.string.FacebooK_Interestitial_Ads));
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                //Toast.makeText(context, "Interstitial ad displayed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                //Toast.makeText(context, "Interstitial ad dismissed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
               // Toast.makeText(context, "Interstitial ad failed to load:", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                // Show the ad
                if(interstitialAd == null || !interstitialAd.isAdLoaded()) {
                    return;
                }
                if(interstitialAd.isAdInvalidated()) {
                    return;
                }
                interstitialAd.show();

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                //Toast.makeText(context, "Interstitial ad clicked!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
               // Toast.makeText(context, "Interstitial ad impression logged!", Toast.LENGTH_SHORT).show();
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

}
