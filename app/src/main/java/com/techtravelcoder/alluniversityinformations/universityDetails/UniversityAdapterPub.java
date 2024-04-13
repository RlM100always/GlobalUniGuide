package com.techtravelcoder.alluniversityinformations.universityDetails;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.ads.App;
import com.techtravelcoder.alluniversityinformations.web.UniversityWebActivity;

import java.util.ArrayList;
import java.util.Random;

import co.notix.interstitial.NotixInterstitial;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;

public class UniversityAdapterPub extends RecyclerView.Adapter<UniversityAdapterPub.UniViewHolder> {
    Context context;
    ArrayList<UniversityModel>list;


    public UniversityAdapterPub(Context context, ArrayList<UniversityModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public UniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.university_public_design, parent, false);
            return new UniViewHolder(view);

    }
    public void searchListPub(ArrayList<UniversityModel> filterlist){
        list=filterlist;
        notifyDataSetChanged();

    }


    @Override
    public void onBindViewHolder(@NonNull UniViewHolder holder, int position) {
        UniversityModel universityModel=list.get(position);
        holder.uniName.setText(universityModel.getUniName());
        Glide.with(context).load(universityModel.getUniImageLink()).into(holder.uniImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UniversityWebActivity.class);
                intent.putExtra("link",universityModel.getUniWebLink());
                intent.putExtra("name",universityModel.getUniName());
                intent.putExtra("country",universityModel.getContryName());

                context.startActivity(intent);
                Random random=new Random();
                int num = random.nextInt(6)+1;
                if(num==3){
                    //FacebookInterestitialAds.loadAds(context);
                    App.interstitialLoader.doOnNextAvailable(result -> {
                        if (result != null) {
                            NotixInterstitial.Companion.show(result);
                        }
                        return Unit.INSTANCE;
                    });

                }
                if(num==5){
                    App.interstitialLoader.doOnNextAvailable(result -> {
                        if (result != null) {
                            NotixInterstitial.Companion.show(result);
                        }
                        return Unit.INSTANCE;
                    });

                }
                if(num==4){
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
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UniViewHolder extends RecyclerView.ViewHolder {
        TextView uniName;
        CircleImageView uniImage;
        public UniViewHolder(@NonNull View itemView) {
            super(itemView);
            uniImage=itemView.findViewById(R.id.public_unidesign_img);
            uniName=itemView.findViewById(R.id.public_unidesign_name);
        }
    }
}
