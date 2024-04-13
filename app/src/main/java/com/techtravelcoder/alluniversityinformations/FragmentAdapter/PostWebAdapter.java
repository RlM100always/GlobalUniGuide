package com.techtravelcoder.alluniversityinformations.FragmentAdapter;

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
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.FragmentModel.MainPostModel;
import com.techtravelcoder.alluniversityinformations.postDetails.PostWebViewActivity;

import java.util.ArrayList;

public class PostWebAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MainPostModel> list;
    private int viewTypeToShow;

    private static final int VIEW_TYPE_1 = 1;
    private static final int VIEW_TYPE_2 = 2;

    public PostWebAdapter(Context context, ArrayList<MainPostModel> list) {
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
                itemView = inflater.inflate(R.layout.post_design_3, parent, false);
                return new PostWebAdapter.ViewHolderType1(itemView);
            case VIEW_TYPE_2:
                itemView = inflater.inflate(R.layout.post_design_4, parent, false);
                return new PostWebAdapter.ViewHolderType2(itemView);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MainPostModel mainPostModel = list.get(position);

        switch (holder.getItemViewType()) {

            case VIEW_TYPE_1:
                PostWebAdapter.ViewHolderType1 viewHolder1 = (PostWebAdapter.ViewHolderType1) holder;
                bindViewHolderType1(viewHolder1, mainPostModel);
                break;
            case VIEW_TYPE_2:
                PostWebAdapter.ViewHolderType2 viewHolder2 = (PostWebAdapter.ViewHolderType2) holder;
                bindViewHolderType2(viewHolder2, mainPostModel);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void bindViewHolderType1(PostWebAdapter.ViewHolderType1 holder, MainPostModel mainPostModel) {
        holder.title1.setText(mainPostModel.getTitle());
        holder.view1.setText("read "+String.valueOf(mainPostModel.getViews()+" times"));
        Glide.with(context).load(mainPostModel.getImage()).into(holder.img1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PostWebViewActivity.class);
                intent.putExtra("postId",mainPostModel.getPostId());
                intent.putExtra("label",mainPostModel.getLabel());
                intent.putExtra("num",mainPostModel.getViews());
                intent.putExtra("key",mainPostModel.getKey());


                context.startActivity(intent);
            }
        });
    }

    private void bindViewHolderType2(PostWebAdapter.ViewHolderType2 holder, MainPostModel mainPostModel) {
        holder.title2.setText(mainPostModel.getTitle());
        holder.view2.setText("read "+String.valueOf(mainPostModel.getViews()+" times"));
        Glide.with(context).load(mainPostModel.getImage()).into(holder.img2);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PostWebViewActivity.class);
                intent.putExtra("postId",mainPostModel.getPostId());
                intent.putExtra("label",mainPostModel.getLabel());
                intent.putExtra("num",mainPostModel.getViews());
                intent.putExtra("key",mainPostModel.getKey());

                context.startActivity(intent);
            }
        });

    }

    private static class ViewHolderType1 extends RecyclerView.ViewHolder {
        ImageView img1;
        TextView  title1,view1;

        ViewHolderType1(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.post_design_image_id3);
            title1 = itemView.findViewById(R.id.post_design_title3);
            view1=itemView.findViewById(R.id.post_design_views3);
        }
    }

    private static class ViewHolderType2 extends RecyclerView.ViewHolder {
        ImageView img2;
        TextView  title2,view2;

        ViewHolderType2(@NonNull View itemView) {
            super(itemView);
            img2 = itemView.findViewById(R.id.post_design_image_id4);
            title2 = itemView.findViewById(R.id.post_design_title4);
            view2=itemView.findViewById(R.id.post_design_views4);
        }
    }
}
