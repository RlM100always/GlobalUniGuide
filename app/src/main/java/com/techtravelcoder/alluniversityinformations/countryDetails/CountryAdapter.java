package com.techtravelcoder.alluniversityinformations.countryDetails;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.ads.App;
import com.techtravelcoder.alluniversityinformations.universityDetails.UniversityActivity;

import java.util.ArrayList;
import java.util.Random;

import co.notix.interstitial.NotixInterstitial;
import kotlin.Unit;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {

    Context context;
    ArrayList<CountryModel>list;

    public CountryAdapter(Context context, ArrayList<CountryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.country_design,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CountryModel countryModel=list.get(position);

        holder.name.setText(countryModel.getName());
        Glide.with(context).load(countryModel.getImageLink()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Intent intent=new Intent(context, UniversityActivity.class);
                    intent.putExtra("name",countryModel.getName());
                    context.startActivity(intent);
                    Random random=new Random();
                }



            }
        });


    }

    public void searchLists(ArrayList<CountryModel> filterlist){
        list=filterlist;

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.country_image_id);
            name=itemView.findViewById(R.id.country_name_id);
        }
    }
}
