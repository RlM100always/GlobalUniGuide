package com.techtravelcoder.uniinfoadmin.university;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techtravelcoder.uniinfoadmin.R;

import java.util.ArrayList;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.UniViewHolder> {
    Context context;
    ArrayList<UniversityModel>list;

    public UniversityAdapter(Context context, ArrayList<UniversityModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UniversityAdapter.UniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.university_design,parent,false);
        return new UniViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniversityAdapter.UniViewHolder holder, int position) {
        UniversityModel universityModel=list.get(position);
        holder.uniName.setText(universityModel.getUniName());
        Glide.with(context).load(universityModel.getUniImageLink()).into(holder.uniImage);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UniViewHolder extends RecyclerView.ViewHolder {
        TextView uniName;
        ImageView uniImage;
        public UniViewHolder(@NonNull View itemView) {
            super(itemView);
            uniImage=itemView.findViewById(R.id.uni_design_image_id);
            uniName=itemView.findViewById(R.id.uni_design_uniname);
        }
    }
}
