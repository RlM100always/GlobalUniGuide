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
import com.techtravelcoder.alluniversityinformations.FragmentModel.CategoryModel;
import com.techtravelcoder.alluniversityinformations.FragmentModel.MainPostModel;
import com.techtravelcoder.alluniversityinformations.postDetails.CategoryPostActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CategoryModel> list;
    private int viewTypeToShow;

    private static final int VIEW_TYPE_1 = 1;
    private static final int VIEW_TYPE_2 = 2;

    public CategoryAdapter(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
        this.viewTypeToShow = VIEW_TYPE_1; // Default view type to show
    }

    public void searchLists(ArrayList<CategoryModel> filterlist) {
        list = filterlist;
        notifyDataSetChanged();
    }

    public void setViewTypeToShow(int viewType) {
        this.viewTypeToShow = viewType;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;

        switch (viewType) {
            case VIEW_TYPE_1:
                itemView = inflater.inflate(R.layout.category_design, parent, false);
                return new ViewHolderType1(itemView);
            case VIEW_TYPE_2:
                itemView = inflater.inflate(R.layout.category_design_1, parent, false);
                return new ViewHolderType2(itemView);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryModel categoryModel = list.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_1:
                ViewHolderType1 viewHolder1 = (ViewHolderType1) holder;
                bindViewHolderType1(viewHolder1, categoryModel);
                break;
            case VIEW_TYPE_2:
                ViewHolderType2 viewHolder2 = (ViewHolderType2) holder;
                bindViewHolderType2(viewHolder2, categoryModel);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypeToShow; // Return the current view type to show
    }

    private void bindViewHolderType1(ViewHolderType1 holder, CategoryModel categoryModel) {
        holder.title1.setText(categoryModel.getName());
        holder.label1.setText(categoryModel.getLabel());
        Glide.with(context).load(categoryModel.getImageLink()).into(holder.img1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CategoryPostActivity.class);
                intent.putExtra("id",categoryModel.getId());
                intent.putExtra("title",categoryModel.getLabel());
                if(categoryModel.getCategoryType()!=null){
                    intent.putExtra("type",categoryModel.getCategoryType());
                }

                context.startActivity(intent);
            }
        });
    }

    private void bindViewHolderType2(ViewHolderType2 holder, CategoryModel categoryModel) {
        holder.title2.setText(categoryModel.getName());
        holder.label2.setText(categoryModel.getLabel());
        Glide.with(context).load(categoryModel.getImageLink()).into(holder.img2);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CategoryPostActivity.class);
                intent.putExtra("id",categoryModel.getId());
                intent.putExtra("title",categoryModel.getLabel());
                if(categoryModel.getCategoryType()!=null){
                    intent.putExtra("type",categoryModel.getCategoryType());
                }

                context.startActivity(intent);
            }
        });

    }

    private static class ViewHolderType1 extends RecyclerView.ViewHolder {
        ImageView img1;
        TextView label1, title1;

        ViewHolderType1(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.category_design_image_id);
            label1 = itemView.findViewById(R.id.category_design_label_id);
            title1 = itemView.findViewById(R.id.category_design_title_id);
        }
    }

    private static class ViewHolderType2 extends RecyclerView.ViewHolder {
        ImageView img2;
        TextView label2, title2;

        ViewHolderType2(@NonNull View itemView) {
            super(itemView);
            img2 = itemView.findViewById(R.id.category_design_image_id);
            label2 = itemView.findViewById(R.id.category_design_label_id);
            title2 = itemView.findViewById(R.id.category_design_title_id);
        }
    }
}
