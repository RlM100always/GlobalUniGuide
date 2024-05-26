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
import com.techtravelcoder.alluniversityinformations.ads.ADSSetUp;
import com.techtravelcoder.alluniversityinformations.ads.App;
import com.techtravelcoder.alluniversityinformations.web.UniversityWebActivity;

import java.util.ArrayList;
import java.util.Random;

import co.notix.interstitial.NotixInterstitial;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;

public class UniversityAdapterWeb extends RecyclerView.Adapter<UniversityAdapterWeb.UniViewHolder> {
    Context context;
    ArrayList<UniversityModel>list;


    public UniversityAdapterWeb(Context context, ArrayList<UniversityModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public UniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.university_suggest_design, parent, false);
            return new UniViewHolder(view);

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
                ADSSetUp.adsType2(context);
                intent.putExtra("name",universityModel.getUniName());
                intent.putExtra("link",universityModel.getUniWebLink());
                intent.putExtra("country",universityModel.getContryName());
                intent.putExtra("key",universityModel.getKey());
                intent.putExtra("bookmark",universityModel.getPostLoves());
                intent.putExtra("reviewers",universityModel.getRatingNum());
                intent.putExtra("avgrate",universityModel.getAvgRating());


                context.startActivity(intent);

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
            uniImage=itemView.findViewById(R.id.suggest_public_unidesign_img);
            uniName=itemView.findViewById(R.id.suggest_public_unidesign_name);
        }
    }
}
