package com.techtravelcoder.alluniversityinformations.books;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
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
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.ads.ADSSetUp;
import com.techtravelcoder.alluniversityinformations.pdf.DatabaseHelper;
import com.techtravelcoder.alluniversityinformations.pdf.PDFShowActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookPostAdapter extends RecyclerView.Adapter<BookPostAdapter.NewViewHolder> {


    Context context;
    int checker;
    ArrayList<BookModel>bookList;
    private DatabaseHelper databaseHelper;

    public BookPostAdapter(Context context, ArrayList<BookModel> bookList,int checker) {
        databaseHelper = new DatabaseHelper(context);

        this.context = context;
        this.bookList = bookList;
        this.checker=checker;
    }

    private void goToAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
    private boolean isStoragePermissionGranted() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true; // Permission is granted
        } else {
            // Permission is not granted, redirect to settings
            goToAppSettings();
            return false;
        }
    }

    public void searchLists(ArrayList<BookModel> filterlist) {
        bookList=filterlist;
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
        BookModel book=bookList.get(position);
        holder.bName.setText(book.getBookName());
        Glide.with(context).load(book.getBookImageLink()).into(holder.bImage);
        if(checker==1){
            holder.bCategory.setVisibility(View.GONE);
        }
        if(checker==2){

            if(book.getBookCategoryName() != null){
                holder.bCategory.setVisibility(View.VISIBLE);
                holder.bCategory.setText(book.getBookCategoryName());
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
            if (book.getBookPostDate().equals(date)) {
                holder.bDate.setText("Publish : "+"Today");
                holder.bDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.deepGreen));
            }
            else if(book.getBookPostDate().equals(previousDate)){
                holder.bDate.setText("Publish : "+"Yesterday");
                holder.bDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.deepYellow));
            }
            else if (book.getBookPostDate().equals(oneWeekAgoDate)) {
                holder.bDate.setText("Publish : "+"1 Weak Ago");
                holder.bDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
            } else {
                holder.bDate.setText("Publish : "+book.getBookPostDate());
                holder.bDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.whiteTextColor1));

            }
        }
        if(checker==3){
            holder.deleteImg.setVisibility(View.VISIBLE);
            holder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertObj = new AlertDialog.Builder(context);
                    alertObj.setTitle(Html.fromHtml("<font color='#000000'>Confirm Exit...ℹ️</font>"));
                    alertObj.setMessage(Html.fromHtml("<font color='#000000'>Do you want to remove this book from bookmark list??</font>"));

                    alertObj.setPositiveButton(Html.fromHtml("<font color='#FF00FF'>✅Yes</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FirebaseDatabase.getInstance().getReference("Book Details").child(book.getBookKey()).child("bookmarkbook")
                                    .child(FirebaseAuth.getInstance().getUid()).child("userBookmark").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Successfully Remove From The BookMark List", Toast.LENGTH_SHORT).show();


                                        }
                                    });

                        }
                    });
                    alertObj.setNegativeButton(Html.fromHtml("<font color='#FF00FF'>❌No</font>"), new DialogInterface.OnClickListener() {
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

            if(book.getBookCategoryName() != null){
                holder.bCategory.setVisibility(View.VISIBLE);
                holder.bCategory.setText(book.getBookCategoryName());
            }


        }
        if(checker==4){

            if(book.getBookCategoryName() != null){
                holder.bCategory.setVisibility(View.VISIBLE);
                holder.bCategory.setText(book.getBookCategoryName());
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

        if(book.getBookPageNo()!=null  && !book.getBookPageNo().equals("")){
            (holder).page.setVisibility(View.VISIBLE);
            ( holder).page.setText(book.getBookPageNo()+" Page");
        }
        if(book.getBookMbSize()!=null  && !book.getBookMbSize().equals("")){
            ( holder).mb.setVisibility(View.VISIBLE);
            ( holder).mb.setText(book.getBookMbSize()+" Mb");
        }
        if(book.getBookCategoryType()!=null){
            ( holder).type.setVisibility(View.VISIBLE);
            ( holder).type.setText(book.getBookCategoryType());
        }
        if(book.getBookLanguageType()!=null){
            ( holder).language.setVisibility(View.VISIBLE);
            ( holder).language.setText(book.getBookLanguageType());
        }



         holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Inflate the custom layout view
                LayoutInflater inflater = LayoutInflater.from(context);
                final View view = inflater.inflate(R.layout.on_off, null);
                TextView offline=view.findViewById(R.id.offline_id);
                TextView onliine=view.findViewById(R.id.online_id);

                builder.setView(view);


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Drawable drawables = ContextCompat.getDrawable(context, R.drawable.alert_back);
                alertDialog.getWindow().setBackgroundDrawable(drawables);
                offline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isStoragePermissionGranted()){
                            if(FirebaseAuth.getInstance().getUid()!=null){
                                if(book.getBookPriceType()!=null){
                                    if(!book.getBookPriceType().equals("Premium")){
                                        String fileUrl = "https://drive.google.com/uc?export=download&id=" + book.getBookDriveurl();
                                        if(databaseHelper.fileExists(fileUrl)){
                                            ADSSetUp.adsType1(context);

                                            Intent intent=new Intent(context, PDFShowActivity.class);
                                            intent.putExtra("fUrl",book.getBookDriveurl());
                                            intent.putExtra("fName",book.getBookKey());
                                            intent.putExtra("bookName",book.getBookName());
                                            intent.putExtra("iUrl",book.getBookImageLink());

                                            context.startActivity(intent);

                                        }
                                        else {
                                            Toast.makeText(context, "Please Download Books From Online First!!!!", Toast.LENGTH_SHORT).show();
                                        }

                                    }else {
                                        Toast.makeText(context, "This Book is Premium", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    String fileUrl = "https://drive.google.com/uc?export=download&id=" + book.getBookDriveurl();

                                    if(databaseHelper.fileExists(fileUrl)){
                                        ADSSetUp.adsType1(context);
                                        Intent intent=new Intent(context, PDFShowActivity.class);
                                        intent.putExtra("fUrl",book.getBookDriveurl());
                                        intent.putExtra("fName",book.getBookKey());
                                        intent.putExtra("bookName",book.getBookName());
                                        intent.putExtra("iUrl",book.getBookImageLink());
                                        context.startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(context, "Please Download Books From Online First!!!!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                alertDialog.dismiss();


                            }
                            else {
                                Toast.makeText(context, "Please Login First", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        }
                        else {
                            Toast.makeText(context, "Storage permission is required", Toast.LENGTH_SHORT).show();


                        }

                    }
                });
                onliine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(FirebaseAuth.getInstance().getUid()!=null){
                            if(book.getBookPriceType()!=null){
                                if(!book.getBookPriceType().equals("Premium")){
                                    ADSSetUp.adsType1(context);

                                    Intent intent=new Intent(context, PDFShowActivity.class);
                                    intent.putExtra("fUrl",book.getBookDriveurl());
                                    intent.putExtra("fName",book.getBookKey());
                                    intent.putExtra("bookName",book.getBookName());
                                    intent.putExtra("iUrl",book.getBookImageLink());
                                    intent.putExtra("check",2);

                                    context.startActivity(intent);
                                    alertDialog.dismiss();
                                }else {
                                    Toast.makeText(context, "This Book is Premium", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();

                                }
                            }
                            alertDialog.dismiss();

                        }else {
                            Toast.makeText(context, "Please Login First", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }

                    }
                });

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
        private TextView mb,page,type,language,status ;

        LinearLayout size;
        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            bName=itemView.findViewById(R.id.lay_book_post_name_id);
            bImage=itemView.findViewById(R.id.lay_book_post_iamge_id);
            bDate=itemView.findViewById(R.id.lay_book_post_date_id);
            deleteImg=itemView.findViewById(R.id.delete_iamge_ids);
            bCategory=itemView.findViewById(R.id.lay_book_post_category_id);
            size=itemView.findViewById(R.id.ll_id_book_post_design);

            mb=itemView.findViewById(R.id.book_mb_id);
            page=itemView.findViewById(R.id.book_pageno_id);
            type=itemView.findViewById(R.id.book_type_id);
            language=itemView.findViewById(R.id.book_language_id);



        }
    }
}
