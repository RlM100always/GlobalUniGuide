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
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.ads.ADSSetUp;
import com.techtravelcoder.alluniversityinformations.web.UniversityWebActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        if(universityModel.getAvgRating()!=null){
            holder.rating.setVisibility(View.VISIBLE);
            String formattedNumber = String.format("%.2f", universityModel.getAvgRating());
            holder.rating.setText(formattedNumber+" ");
        }else {
            holder.rating.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UniversityWebActivity.class);
                ADSSetUp.adsType1(context);

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
        TextView uniName,rating;
        CircleImageView uniImage;
        public UniViewHolder(@NonNull View itemView) {
            super(itemView);
            uniImage=itemView.findViewById(R.id.public_unidesign_img);
            uniName=itemView.findViewById(R.id.public_unidesign_name);
            rating=itemView.findViewById(R.id.public_uni_design_rating_id);

        }
    }
}
