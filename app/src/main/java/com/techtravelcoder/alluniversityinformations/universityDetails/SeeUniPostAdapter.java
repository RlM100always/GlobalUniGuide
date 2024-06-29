package com.techtravelcoder.alluniversityinformations.universityDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.FragmentModel.RatingModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeeUniPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<RatingModel> list;
    private int viewTypeToShow;
    private static final int VIEW_TYPE_1 = 1;
    private static final int VIEW_TYPE_2 = 2;

    public SeeUniPostAdapter(Context context, ArrayList<RatingModel> list) {
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
                itemView = inflater.inflate(R.layout.reviewers_design, parent, false);
                return new SeeUniPostAdapter.ViewHolderType1(itemView);
            case VIEW_TYPE_2:
                itemView = inflater.inflate(R.layout.reviewers_design, parent, false);
                return new SeeUniPostAdapter.ViewHolderType2(itemView);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RatingModel ratingModel = list.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_1:
                SeeUniPostAdapter.ViewHolderType1 viewHolder1 = (SeeUniPostAdapter.ViewHolderType1) holder;
                bindViewHolderType1(viewHolder1, ratingModel);
                break;
            case VIEW_TYPE_2:
                SeeUniPostAdapter.ViewHolderType2 viewHolder2 = (SeeUniPostAdapter.ViewHolderType2) holder;
                bindViewHolderType2(viewHolder2, ratingModel);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    private void bindViewHolderType1(SeeUniPostAdapter.ViewHolderType1 holder, RatingModel ratingModel) {
      holder.name.setText(ratingModel.getName());
      holder.date.setText(ratingModel.getDate());
      if(ratingModel.getImage() != null){
          Glide.with(context).load(ratingModel.getImage()).into(holder.img);
      }

        holder.text.setText(ratingModel.getText());
        Double myDouble = ratingModel.getRate();
        float fl= myDouble.floatValue();
        holder.ratingBar.setRating(fl);
        holder.ratingBar.setIsIndicator(true);



    }
    private void bindViewHolderType2(SeeUniPostAdapter.ViewHolderType2 holder, RatingModel universityModel) {

    }

    private static class ViewHolderType1 extends RecyclerView.ViewHolder {

        TextView name,date,text;
        RatingBar ratingBar;
        CircleImageView img;

        ViewHolderType1(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.reviewers_name_id);
            date=itemView.findViewById(R.id.reviewers_date_id);
            text=itemView.findViewById(R.id.reviewers_text_id);
            ratingBar=itemView.findViewById(R.id.reviewers_ratingbar_id);
            img=itemView.findViewById(R.id.cv_image_id);



        }
    }

    private static class ViewHolderType2 extends RecyclerView.ViewHolder {

        TextView name,date,text;
        RatingBar ratingBar;

        ViewHolderType2(@NonNull View itemView) {
            super(itemView);


        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
