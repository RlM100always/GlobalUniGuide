package com.techtravelcoder.alluniversityinformations.books;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.onesignal.common.AndroidSupportV4Compat;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.pdf.PDFShowActivity;
import com.techtravelcoder.alluniversityinformations.postDetails.PostHandleActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookPostAdapter extends RecyclerView.Adapter<BookPostAdapter.NewViewHolder> {


    Context context;
    int checker;
    ArrayList<BookPostModel>bookList;

    public BookPostAdapter(Context context, ArrayList<BookPostModel> bookList,int checker) {
        this.context = context;
        this.bookList = bookList;
        this.checker=checker;
    }

    @NonNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.lay_book_post_design,parent,false);
        return new NewViewHolder(view);
    }
    public int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder holder, int position) {
        BookPostModel bookPostModel=bookList.get(position);
        holder.bName.setText(bookPostModel.getBookName());
        Glide.with(context).load(bookPostModel.getBookImageLink()).into(holder.bImage);
        if(checker==1){
            holder.bCategory.setVisibility(View.GONE);
        }
        if(checker==2){

            if(bookPostModel.getBookCategoryName() != null){
                holder.bCategory.setVisibility(View.VISIBLE);
                holder.bCategory.setText(bookPostModel.getBookCategoryName());
            }

            Calendar calendar = Calendar.getInstance();
            Date times=calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
            String date = sdf.format(times);

            // Get the previous date
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            Date previousDay = calendar.getTime();
            String previousDate = sdf.format(previousDay);

            //onweak

            // Get the date one week ago
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            Date oneWeekAgo = calendar.getTime();
            String oneWeekAgoDate = sdf.format(oneWeekAgo);

            holder.bDate.setVisibility(View.VISIBLE);
            if (bookPostModel.getBookPostDate().equals(date)) {
                holder.bDate.setText("Publish : "+"Today");
                holder.bDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.yellow));
            }else if(bookPostModel.getBookPostDate().equals(previousDate)){
                holder.bDate.setText("Publish : "+"Yesterday");
                holder.bDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heading));
            } else if (bookPostModel.getBookPostDate().equals(oneWeekAgoDate)) {
                holder.bDate.setText("Publish : "+"1 Weak Ago");
                holder.bDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.my_primary));
            } else {
                holder.bDate.setText("Publish : "+bookPostModel.getBookPostDate());
            }
        }
        if(checker==3){
            holder.deleteImg.setVisibility(View.VISIBLE);
            holder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertObj = new AlertDialog.Builder(context);
                    alertObj.setTitle(Html.fromHtml("<font color='#000000'>Confirm Removal...ℹ️</font>"));
                    alertObj.setMessage(Html.fromHtml("<font color='#000000'>ℹ️ Do you want to remove this Book from the BookMark list ❓❓</font>"));

                    alertObj.setPositiveButton(Html.fromHtml("<font color='#000000'>✅Yes</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FirebaseDatabase.getInstance().getReference("Book Details").child(bookPostModel.getBookKey()).child("bookmark")
                                    .child(FirebaseAuth.getInstance().getUid()).setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Successfully remove from the BookMArk List", Toast.LENGTH_SHORT).show();

                                            Intent intent=new Intent(context, BookPostActivity.class);
                                            intent.putExtra("key","@");
                                            context.startActivity(intent);
                                            ((Activity)context).finish();
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
            if(bookPostModel.getBookCategoryName() != null){
                holder.bCategory.setVisibility(View.VISIBLE);
                holder.bCategory.setText(bookPostModel.getBookCategoryName());
            }


        }
        if(checker==4){

            if(bookPostModel.getBookCategoryName() != null){
                holder.bCategory.setVisibility(View.VISIBLE);
                holder.bCategory.setText(bookPostModel.getBookCategoryName());
            }

            int widthInDp = 135;
            int widthInPx = dpToPx(widthInDp);

            // Create layout params with desired width and current height
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    widthInPx,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            holder.size.setLayoutParams(params);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PDFShowActivity.class);
                intent.putExtra("fUrl",bookPostModel.getBookDriveurl());
                intent.putExtra("fName",bookPostModel.getBookKey());
                intent.putExtra("bookName",bookPostModel.getBookName());
                intent.putExtra("iUrl",bookPostModel.getBookImageLink());


                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class NewViewHolder extends RecyclerView.ViewHolder {
        private TextView bName,bDate,bCategory;
        private ImageView bImage;
        private TextView upadte;
        private ImageView deleteImg;
        LinearLayout size;
        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            bName=itemView.findViewById(R.id.lay_book_post_name_id);
            bImage=itemView.findViewById(R.id.lay_book_post_iamge_id);
            bDate=itemView.findViewById(R.id.lay_book_post_date_id);
            deleteImg=itemView.findViewById(R.id.delete_iamge_ids);
            bCategory=itemView.findViewById(R.id.lay_book_post_category_id);
            size=itemView.findViewById(R.id.ll_id_book_post_design);



        }
    }
}
