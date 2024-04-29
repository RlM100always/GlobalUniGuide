package com.techtravelcoder.alluniversityinformations.universityDetails;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.FragmentModel.MainPostModel;
import com.techtravelcoder.alluniversityinformations.postDetails.PostHandleActivity;
import com.techtravelcoder.alluniversityinformations.web.UniversityWebActivity;

import java.util.ArrayList;

public class ReBookMarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<UniversityModel> list;
    private int viewTypeToShow;
    private static final int VIEW_TYPE_1 = 1;
    private static final int VIEW_TYPE_2 = 2;

    public ReBookMarkAdapter(Context context, ArrayList<UniversityModel> list) {
        this.context = context;
        this.list = list;
        this.viewTypeToShow = VIEW_TYPE_1;
    }

    public void setViewTypeToShow(int viewType) {
        this.viewTypeToShow = viewType;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypeToShow; // Return the current view type to show
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        switch (viewType) {
            case VIEW_TYPE_1:
                itemView = inflater.inflate(R.layout.recent_uni_design, parent, false);
                return new ReBookMarkAdapter.ViewHolderType1(itemView);
            case VIEW_TYPE_2:
                itemView = inflater.inflate(R.layout.recent_uni_design, parent, false);
                return new ReBookMarkAdapter.ViewHolderType2(itemView);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UniversityModel universityModel = list.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_1:
                ReBookMarkAdapter.ViewHolderType1 viewHolder1 = (ReBookMarkAdapter.ViewHolderType1) holder;
                bindViewHolderType1(viewHolder1, universityModel);
                break;
            case VIEW_TYPE_2:
                ReBookMarkAdapter.ViewHolderType2 viewHolder2 = (ReBookMarkAdapter.ViewHolderType2) holder;
                bindViewHolderType2(viewHolder2, universityModel);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }
    private void bindViewHolderType1(ReBookMarkAdapter.ViewHolderType1 holder, UniversityModel universityModel) {
        holder.delete.setVisibility(View.GONE);
        if(universityModel.getAvgRating()!=null){
            holder.rating.setVisibility(View.VISIBLE);
            holder.rating.setText(universityModel.getAvgRating()+" ");
        }else {
            holder.rating.setVisibility(View.GONE);
        }
        holder.uni.setText(universityModel.getUniName());
        holder.con.setText(universityModel.getContryName());
        if(universityModel.getDate()!=null){
            holder.date.setText(universityModel.getDate());
        }else {
            holder.date.setVisibility(View.GONE);
        }
        Glide.with(context).load(universityModel.getUniImageLink()).into(holder.uniImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UniversityWebActivity.class);
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
    private void bindViewHolderType2(ReBookMarkAdapter.ViewHolderType2 holder, UniversityModel universityModel) {
        holder.date.setVisibility(View.GONE);
        if(universityModel.getAvgRating()!=null){
            holder.rating.setVisibility(View.VISIBLE);
            holder.rating.setText(universityModel.getAvgRating()+" ");
        }else {
            holder.rating.setVisibility(View.GONE);
        }
        holder.uni.setText(universityModel.getUniName());
        holder.con.setText(universityModel.getContryName());
        Glide.with(context).load(universityModel.getUniImageLink()).into(holder.uniImage);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertObj = new AlertDialog.Builder(context);
                alertObj.setTitle(Html.fromHtml("<font color='#000000'>Confirm Removal...ℹ️</font>"));
                alertObj.setMessage(Html.fromHtml("<font color='#000000'>ℹ️ Do you want to remove this University from BookMark list ❓❓</font>"));

                alertObj.setPositiveButton(Html.fromHtml("<font color='#000000'>✅Yes</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey()).child("favorite")
                                .child(FirebaseAuth.getInstance().getUid()).setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Successfully remove from the BookMark List", Toast.LENGTH_SHORT).show();
                                        FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey()).child("postLoves").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Long num=snapshot.getValue(Long.class);
                                                // Toast.makeText(context, ""+num, Toast.LENGTH_SHORT).show();
                                                if(snapshot.exists()){
                                                    FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey()).child("postLoves").setValue(num-1L);

                                                }else {
                                                    FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey()).child("postLoves").setValue(1L);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        Intent intent=new Intent(context, ReBookMarkActivity.class);
                                        intent.putExtra("check",2);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                    }
                                });

                    }
                });
                alertObj.setNegativeButton(Html.fromHtml("<font color='#000000'>❌No</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = alertObj.create();
                dialog.show();

                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.alert_back);
                dialog.getWindow().setBackgroundDrawable(drawable);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UniversityWebActivity.class);
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

    private static class ViewHolderType1 extends RecyclerView.ViewHolder {

        ImageView uniImage;
        TextView uni,con,date,delete,rating;

        ViewHolderType1(@NonNull View itemView) {
            super(itemView);
            rating=itemView.findViewById(R.id.rebook_uni_rate_id);
            uniImage=itemView.findViewById(R.id.re_uni_img_id);
            uni=itemView.findViewById(R.id.re_uni_uni_id);
            con=itemView.findViewById(R.id.re_uni_country_id);
            date=itemView.findViewById(R.id.re_uni_date_id);
            delete=itemView.findViewById(R.id.re_uni_delete_id);


        }
    }

    private static class ViewHolderType2 extends RecyclerView.ViewHolder {

        ImageView uniImage;
        TextView uni,con,date,delete,rating;
        ViewHolderType2(@NonNull View itemView) {
            super(itemView);
            uniImage=itemView.findViewById(R.id.re_uni_img_id);
            uni=itemView.findViewById(R.id.re_uni_uni_id);
            con=itemView.findViewById(R.id.re_uni_country_id);
            date=itemView.findViewById(R.id.re_uni_date_id);
            delete=itemView.findViewById(R.id.re_uni_delete_id);
            rating=itemView.findViewById(R.id.rebook_uni_rate_id);

        }
    }
    public void searchLists(ArrayList<UniversityModel> filterlist) {
        list = filterlist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
