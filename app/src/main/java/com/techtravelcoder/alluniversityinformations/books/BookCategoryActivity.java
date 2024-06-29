package com.techtravelcoder.alluniversityinformations.books;

import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;

import java.util.ArrayList;

public class BookCategoryActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private ArrayList<BookCategoryModel>bookCategoryList;
    private BookCategoryAdapter bookCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_category);

        //initialize all attributes/variable

        searchView=findViewById(R.id.book_categoyr_searhc);
        recyclerView=findViewById(R.id.book_category_recycler_view_id);

        //set statusbar color
        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.whiteTextSideColor1);
        }
        getWindow().setStatusBarColor(color);


        //searchview background check
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.allert_back_upper));



        //retrive category data

        retriveBookCategoryDetailsData();

    }
    private void retriveBookCategoryDetailsData() {
        bookCategoryList=new ArrayList<>();
        bookCategoryAdapter=new BookCategoryAdapter(BookCategoryActivity.this,bookCategoryList);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView.setAdapter(bookCategoryAdapter);
        FirebaseDatabase.getInstance().getReference("Book Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookCategoryList.clear();
                BookCategoryModel bookCategoryModel;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        bookCategoryModel = dataSnapshot.getValue(BookCategoryModel.class);
                        if(bookCategoryModel != null){
                            bookCategoryList.add(0,bookCategoryModel);

                        }

                    }

                }
                bookCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}