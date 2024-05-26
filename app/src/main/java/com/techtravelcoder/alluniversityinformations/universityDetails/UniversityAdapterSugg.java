package com.techtravelcoder.alluniversityinformations.universityDetails;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import kotlin.Unit;

public class UniversityAdapterSugg extends RecyclerView.Adapter<UniversityAdapterSugg.UniViewHolder> {
    Context context;
    ArrayList<UniversityModel>list;


    public UniversityAdapterSugg(Context context, ArrayList<UniversityModel> list) {
        this.context = context;
        this.list = list;

    }

    public void searchListSugg(ArrayList<UniversityModel> filterlist){
        list=filterlist;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public UniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.new_suggest_design, parent, false);
            return new UniViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UniViewHolder holder, int position) {
        UniversityModel universityModel=list.get(position);
        holder.uniName.setText(universityModel.getUniName());
        holder.countryName.setText(universityModel.getContryName());

        if(universityModel.getAvgRating()!=null){
            holder.rating.setVisibility(View.VISIBLE);
            String formattedNumber = String.format("%.2f", universityModel.getAvgRating());
            holder.rating.setText(formattedNumber+" ");
        }else {
            holder.rating.setVisibility(View.GONE);
        }

        Glide.with(context).load(universityModel.getUniImageLink()).into(holder.uniImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UniversityWebActivity.class);
                ADSSetUp.adsType2(context);
                intent.putExtra("link",universityModel.getUniWebLink());
                intent.putExtra("name",universityModel.getUniName());
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
        ImageView uniImage;
        TextView countryName,rating;
        public UniViewHolder(@NonNull View itemView) {
            super(itemView);
            uniImage=itemView.findViewById(R.id.sugg_img_id);
            uniName=itemView.findViewById(R.id.sugg_uni_name_id);
            countryName=itemView.findViewById(R.id.country_id_sugg);
            rating=itemView.findViewById(R.id.sugg_uni_rate_id);
        }
    }
}
