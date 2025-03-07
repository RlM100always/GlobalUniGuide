package com.techtravelcoder.alluniversityinformations.books;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.FragmentModel.MainPostModel;
import com.techtravelcoder.alluniversityinformations.ads.ADSSetUp;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookCategoryAdapter extends RecyclerView.Adapter<BookCategoryAdapter.BookCategoryViewHolder> {

    Context context;
    ArrayList<BookCategoryModel>bookCategoryModelList;
    ArrayList<BookCategoryModel>bookNumber;

    public BookCategoryAdapter(Context context, ArrayList<BookCategoryModel> bookCategoryModelList) {
        this.context = context;
        this.bookCategoryModelList = bookCategoryModelList;
        bookNumber=new ArrayList<>();
    }

    @NonNull
    @Override
    public BookCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =LayoutInflater.from(context).inflate(R.layout.lay_category_design,parent,false);

        return new BookCategoryViewHolder(view);
    }

    public void searchLists(ArrayList<BookCategoryModel> filterlist) {
        bookCategoryModelList=filterlist;
    }


    private void fetchBookDetails(@NonNull BookCategoryViewHolder holder, BookCategoryModel bookCategoryModel) {
        FirebaseDatabase.getInstance().getReference("Book Details")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<BookPostModel> bookNumber = new ArrayList<>(); // Local variable to avoid shared resource issues
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                BookPostModel bookPostModel = dataSnapshot.getValue(BookPostModel.class);
                                if (bookPostModel != null && bookPostModel.getBookCategoryKey().equals(bookCategoryModel.getbCategoryKey())) {
                                    bookNumber.add(bookPostModel);
                                }
                            }
                            holder.availableBooks.setText(bookNumber.size() + " Books");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error
                    }
                });
    }
    @Override
    public void onBindViewHolder(@NonNull BookCategoryViewHolder holder, int position) {

        BookCategoryModel bookCategoryModel=bookCategoryModelList.get(position);
        holder.cName.setText(bookCategoryModel.getbCategoryName());
        Glide.with(context).load(bookCategoryModel.getbCategoryImageLink()).into(holder.cImage);

        fetchBookDetails(holder, bookCategoryModel);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,BookPostActivity.class);
                ADSSetUp.adsType1(context);

                intent.putExtra("key",bookCategoryModel.getbCategoryKey());
                intent.putExtra("category",bookCategoryModel.getbCategoryName());

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return bookCategoryModelList.size();
    }

    public class BookCategoryViewHolder extends RecyclerView.ViewHolder {
        //decalare global attribute

        private CircleImageView cImage;
        private TextView cName,availableBooks;
        public BookCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            cImage=itemView.findViewById(R.id.lay_category_iamge_id);
            cName=itemView.findViewById(R.id.lay_category_name_id);
            availableBooks=itemView.findViewById(R.id.lay_category_total_book_id);

        }
    }
}
