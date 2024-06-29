package com.techtravelcoder.uniinfoadmin.book;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.uniinfoadmin.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookPostAdapter extends RecyclerView.Adapter<BookPostAdapter.NewViewHolder> {


    Context context;
    ArrayList<BookPostModel>bookList;
    String bCataNAme;

    public BookPostAdapter(Context context, ArrayList<BookPostModel> bookList,String bCataNAme) {
        this.context = context;
        this.bookList = bookList;
        this.bCataNAme=bCataNAme;
    }

    @NonNull
    @Override
    public BookPostAdapter.NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.lay_book_post_design,parent,false);
        return new NewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookPostAdapter.NewViewHolder holder, int position) {
        BookPostModel bookPostModel=bookList.get(position);
        holder.bName.setText(bookPostModel.getBookName());
        Glide.with(context).load(bookPostModel.getBookImageLink()).into(holder.bImage);
        holder.upadte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                final View view=LayoutInflater.from(context).inflate(R.layout.book_post_design,null);

                EditText bookName=view.findViewById(R.id.ed_book_post_name_id);
                EditText bookImageLink=view.findViewById(R.id.ed_book_post_image_link_id);
                EditText bookDriveUrl=view.findViewById(R.id.ed_book_post_drive_url_id);

                bookName.setText(bookPostModel.getBookName());
                bookImageLink.setText(bookPostModel.getBookImageLink());
                bookDriveUrl.setText(bookPostModel.getBookDriveurl());



                TextView addBook=view.findViewById(R.id.book_post_add_id);


                builder.setView(view);
                AlertDialog alertDialog= builder.create();
                Drawable drawable= ContextCompat.getDrawable(context,R.drawable.back);
                alertDialog.getWindow().setBackgroundDrawable(drawable);
                alertDialog.show();
                addBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(bookName.getText().toString()) && !TextUtils.isEmpty(bookImageLink.getText().toString()) && !TextUtils.isEmpty(bookDriveUrl.getText().toString())){

                            Calendar calendar = Calendar.getInstance();
                            Date times=calendar.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
                            String date = sdf.format(times);


//

                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Book Details").child(bookPostModel.getBookKey());

                            databaseReference.child("bookName").setValue(bookName.getText().toString());
                            databaseReference.child("bookImageLink").setValue(bookImageLink.getText().toString());
                            databaseReference.child("bookDriveurl").setValue(bookDriveUrl.getText().toString());
                            databaseReference.child("bookName").setValue(bookName.getText().toString());
                            databaseReference.child("bookCategoryName").setValue(bCataNAme);
                            databaseReference.child("bookPostDate").setValue(date);


                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();



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
        private TextView bName;
        private ImageView bImage;
        private TextView upadte;
        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            bName=itemView.findViewById(R.id.lay_book_post_name_id);
            bImage=itemView.findViewById(R.id.lay_book_post_iamge_id);
            upadte=itemView.findViewById(R.id.book_post_update_id);


        }
    }
}
