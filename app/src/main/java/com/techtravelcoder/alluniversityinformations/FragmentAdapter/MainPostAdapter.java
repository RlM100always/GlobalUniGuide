package com.techtravelcoder.alluniversityinformations.FragmentAdapter;

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
import com.techtravelcoder.alluniversityinformations.ads.ADSSetUp;
import com.techtravelcoder.alluniversityinformations.countryDetails.MainActivity;
import com.techtravelcoder.alluniversityinformations.postDetails.PostHandleActivity;
import com.techtravelcoder.alluniversityinformations.postDetails.PostWebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class MainPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MainPostModel> list;
    private int viewTypeToShow;

    private static final int VIEW_TYPE_1 = 1;
    private static final int VIEW_TYPE_2 = 2;
    private static final int VIEW_TYPE_3 = 3;
    private static final int VIEW_TYPE_4 = 4;


    public MainPostAdapter(Context context, ArrayList<MainPostModel> list) {
        this.context = context;
        this.list = list;
        this.viewTypeToShow = VIEW_TYPE_1;
    }

    public void setViewTypeToShow(int viewType) {
        this.viewTypeToShow = viewType;
        notifyDataSetChanged();
    }


    public void searchLists(ArrayList<MainPostModel> filterlist) {
        list = filterlist;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;

        switch (viewType) {
            case VIEW_TYPE_1:
                itemView = inflater.inflate(R.layout.post_design_1, parent, false);
                return new ViewHolderType1(itemView);
            case VIEW_TYPE_2:
                itemView = inflater.inflate(R.layout.post_design_2, parent, false);
                return new ViewHolderType2(itemView);
            case VIEW_TYPE_3:
                itemView = inflater.inflate(R.layout.recent_design, parent, false);
                return new ViewHolderType3(itemView);
            case VIEW_TYPE_4:
                itemView = inflater.inflate(R.layout.favorite_design_4, parent, false);
                return new ViewHolderType4(itemView);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MainPostModel mainPostModel = list.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_1:
                MainPostAdapter.ViewHolderType1 viewHolder1 = (MainPostAdapter.ViewHolderType1) holder;
                bindViewHolderType1(viewHolder1, mainPostModel);
                break;
            case VIEW_TYPE_2:
                MainPostAdapter.ViewHolderType2 viewHolder2 = (MainPostAdapter.ViewHolderType2) holder;
                bindViewHolderType2(viewHolder2, mainPostModel);
                break;
            case VIEW_TYPE_3:
                MainPostAdapter.ViewHolderType3 viewHolder3 = (MainPostAdapter.ViewHolderType3) holder;
                bindViewHolderType3(viewHolder3, mainPostModel);
                break;
            case VIEW_TYPE_4:
                MainPostAdapter.ViewHolderType4 viewHolder4 = (MainPostAdapter.ViewHolderType4) holder;
                bindViewHolderType4(viewHolder4, mainPostModel);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }

    }

    @Override
    public int getItemViewType(int position) {
        return viewTypeToShow; // Return the current view type to show
    }

    private void bindViewHolderType1(MainPostAdapter.ViewHolderType1 holder, MainPostModel mainPostModel) {
        holder.title1.setText(mainPostModel.getTitle());
        holder.label1.setText(mainPostModel.getCategory());
        holder.view1.setText(mainPostModel.getViews()+" views");
        if(mainPostModel.getPostLoves()!=null){
            holder.loves1.setText(mainPostModel.getPostLoves()+" loves");
        }else {
            holder.loves1.setText(0+" loves");

        }

        if(mainPostModel.getRatingNum()!=null && mainPostModel.getAvgRating()!=null){

            holder.reviews1.setText(mainPostModel.getRatingNum()+" reviews");
            String formattedNumber = String.format("%.2f", mainPostModel.getAvgRating());
            holder.star1.setText(formattedNumber+" stars");
        }else {
            holder.reviews1.setText(0+" reviews");
            holder.star1.setText(0+" stars");
        }



        Glide.with(context).load(mainPostModel.getImage()).into(holder.img1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, PostWebViewActivity.class);
                ADSSetUp.adsType2(context);
                intent.putExtra("postId",mainPostModel.getPostId());
                intent.putExtra("label",mainPostModel.getLabel());
                intent.putExtra("num",mainPostModel.getViews());
                intent.putExtra("key",mainPostModel.getKey());
                intent.putExtra("loves",mainPostModel.getPostLoves());
                intent.putExtra("reviewers",mainPostModel.getRatingNum());
                intent.putExtra("avgrate",mainPostModel.getAvgRating());


                context.startActivity(intent);
            }
        });
    }

    private void bindViewHolderType2(MainPostAdapter.ViewHolderType2 holder, MainPostModel mainPostModel) {

        holder.title2.setText(mainPostModel.getTitle());
        holder.label2.setText(mainPostModel.getCategory());
        holder.view2.setText(mainPostModel.getViews()+" views");
        Glide.with(context).load(mainPostModel.getImage()).into(holder.img2);
        if(mainPostModel.getPostLoves()!=null){
            holder.loves2.setText(mainPostModel.getPostLoves()+" loves");
        }else {
            holder.loves2.setText(0+" loves");

        }

        if( mainPostModel.getRatingNum()!=null && mainPostModel.getAvgRating()!=null){

            holder.reviews2.setText(mainPostModel.getRatingNum()+" reviews");
            String formattedNumber = String.format("%.2f", mainPostModel.getAvgRating());
            holder.star2.setText(formattedNumber+" stars");
        }
        else {
            holder.reviews2.setText(0+" reviews");
            holder.star2.setText(0+" stars");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PostWebViewActivity.class);
                ADSSetUp.adsType2(context);
                intent.putExtra("postId",mainPostModel.getPostId());
                intent.putExtra("label",mainPostModel.getLabel());
                intent.putExtra("num",mainPostModel.getViews());
                intent.putExtra("key",mainPostModel.getKey());
                intent.putExtra("loves",mainPostModel.getPostLoves());
                intent.putExtra("reviewers",mainPostModel.getRatingNum());
                intent.putExtra("avgrate",mainPostModel.getAvgRating());
                context.startActivity(intent);
            }
        });

    }

    private void bindViewHolderType3(MainPostAdapter.ViewHolderType3 holder, MainPostModel mainPostModel) {
        holder.title3.setText(mainPostModel.getTitle());
        if(mainPostModel.getDate()!=null){
            holder.date3.setText(mainPostModel.getDate());
        }
        holder.view3.setText(mainPostModel.getViews()+" views");
        holder.category3.setText(mainPostModel.getCategory());
        Glide.with(context).load(mainPostModel.getImage()).into(holder.img3);

        if(mainPostModel.getPostLoves()!=null){
            holder.loves3.setText(mainPostModel.getPostLoves()+" loves");
        }else {
            holder.loves3.setText(0+" loves");

        }

        if(mainPostModel.getRatingNum()!=null && mainPostModel.getAvgRating()!=null){

            holder.reviews3.setText(mainPostModel.getRatingNum()+" reviews");
            String formattedNumber = String.format("%.2f", mainPostModel.getAvgRating());
            holder.star3.setText(formattedNumber+" stars");
        }else {
            holder.reviews3.setText(0+" reviews");
            holder.star3.setText(0+" stars");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PostWebViewActivity.class);
                ADSSetUp.adsType2(context);
                intent.putExtra("postId",mainPostModel.getPostId());
                intent.putExtra("num",mainPostModel.getViews());
                intent.putExtra("key",mainPostModel.getKey());
                intent.putExtra("label",mainPostModel.getLabel());
                intent.putExtra("loves",mainPostModel.getPostLoves());
                intent.putExtra("reviewers",mainPostModel.getRatingNum());
                intent.putExtra("avgrate",mainPostModel.getAvgRating());

                context.startActivity(intent);
            }
        });
    }

    private void bindViewHolderType4(MainPostAdapter.ViewHolderType4 holder, MainPostModel mainPostModel) {
        holder.title4.setText(mainPostModel.getTitle());
        holder.label4.setText(mainPostModel.getCategory());
        holder.view4.setText(mainPostModel.getViews()+" views");
        Glide.with(context).load(mainPostModel.getImage()).into(holder.img4);

        if(mainPostModel.getPostLoves()!=null){
            holder.loves4.setText(mainPostModel.getPostLoves()+" loves");
        }else {
            holder.loves4.setText(0+" loves");

        }

        if(mainPostModel.getRatingNum()!=null && mainPostModel.getAvgRating()!=null){

            holder.reviews4.setText(mainPostModel.getRatingNum()+" reviews");
            String formattedNumber = String.format("%.2f", mainPostModel.getAvgRating());
            holder.star4.setText(formattedNumber+" stars");
        }else {
            holder.reviews4.setText(0+" reviews");
            holder.star4.setText(0+" stars");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PostWebViewActivity.class);
                ADSSetUp.adsType2(context);
                intent.putExtra("postId",mainPostModel.getPostId());
                intent.putExtra("num",mainPostModel.getViews());
                intent.putExtra("key",mainPostModel.getKey());
                intent.putExtra("label",mainPostModel.getLabel());
                intent.putExtra("loves",mainPostModel.getPostLoves());
                intent.putExtra("reviewers",mainPostModel.getRatingNum());
                intent.putExtra("avgrate",mainPostModel.getAvgRating());
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertObj = new AlertDialog.Builder(context);
                alertObj.setTitle(Html.fromHtml("<font color='#000000'>Confirm Removal...ℹ️</font>"));
                alertObj.setMessage(Html.fromHtml("<font color='#000000'>ℹ️ Do you want to remove this post from favorite list ❓❓</font>"));

                alertObj.setPositiveButton(Html.fromHtml("<font color='#000000'>✅Yes</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase.getInstance().getReference("Post").child(mainPostModel.getKey()).child("favorite")
                                .child(FirebaseAuth.getInstance().getUid()).setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Successfully remove from the Favorite List", Toast.LENGTH_SHORT).show();
                                        FirebaseDatabase.getInstance().getReference("Post").child(mainPostModel.getKey()).child("postLoves").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Long num=snapshot.getValue(Long.class);
                                               // Toast.makeText(context, ""+num, Toast.LENGTH_SHORT).show();
                                                if(snapshot.exists()){
                                                    FirebaseDatabase.getInstance().getReference("Post").child(mainPostModel.getKey()).child("postLoves").setValue(num-1L);

                                                }else {
                                                    FirebaseDatabase.getInstance().getReference("Post").child(mainPostModel.getKey()).child("postLoves").setValue(1L);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        Intent intent=new Intent(context, PostHandleActivity.class);
                                        context.startActivity(intent);
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
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    private static class ViewHolderType1 extends RecyclerView.ViewHolder {
        ImageView img1;
        TextView label1, title1,view1,loves1,reviews1,star1;

        ViewHolderType1(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.post_design_image_id);
            label1 = itemView.findViewById(R.id.post_design_category);
            title1 = itemView.findViewById(R.id.post_design_title);
            view1=itemView.findViewById(R.id.post_design_views);
            loves1=itemView.findViewById(R.id.post_design_loves);
            reviews1=itemView.findViewById(R.id.post_design_reviews_num);
            star1=itemView.findViewById(R.id.post_design_star);

        }
    }

    private static class ViewHolderType2 extends RecyclerView.ViewHolder {
        ImageView img2;
        TextView label2, title2,view2,loves2,reviews2,star2;

        ViewHolderType2(@NonNull View itemView) {
            super(itemView);
            img2 = itemView.findViewById(R.id.post_design_image_id);
            label2 = itemView.findViewById(R.id.post_design_category);
            title2 = itemView.findViewById(R.id.post_design_title);
            view2=itemView.findViewById(R.id.post_design_views);
            loves2=itemView.findViewById(R.id.post_design_loves);
            reviews2=itemView.findViewById(R.id.post_design_reviews_num);
            star2=itemView.findViewById(R.id.post_design_star);
        }
    }

    private static class ViewHolderType3 extends RecyclerView.ViewHolder {
        ImageView img3;
        TextView  date3,title3,view3,category3,loves3,reviews3,star3;

        ViewHolderType3(@NonNull View itemView) {
            super(itemView);
            img3 = itemView.findViewById(R.id.recent_design_image_id);
            title3 = itemView.findViewById(R.id.recent_design_title);
            view3=itemView.findViewById(R.id.recent_design_views);
            category3=itemView.findViewById(R.id.recent_design_category);
            date3=itemView.findViewById(R.id.recent_design_date);
            loves3=itemView.findViewById(R.id.recent_design_loves);
            reviews3=itemView.findViewById(R.id.recent_design_reviews_num);
            star3=itemView.findViewById(R.id.recent_design_star);

        }
    }


    private static class ViewHolderType4 extends RecyclerView.ViewHolder {
        ImageView img4;
        TextView label4, title4,view4,delete,loves4,reviews4,star4;

        ViewHolderType4(@NonNull View itemView) {
            super(itemView);
            label4 = itemView.findViewById(R.id.post_design_category);
            img4 = itemView.findViewById(R.id.post_design_image_id);
            title4 = itemView.findViewById(R.id.post_design_title);
            view4=itemView.findViewById(R.id.post_design_views);
            delete=itemView.findViewById(R.id.favorite_post_delete_id);
            loves4=itemView.findViewById(R.id.post_design_loves);
            reviews4=itemView.findViewById(R.id.post_design_reviews_num);
            star4=itemView.findViewById(R.id.post_design_star);

        }
    }

}
