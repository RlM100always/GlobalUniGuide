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
import com.techtravelcoder.alluniversityinformations.FragmentModel.MainPostModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        retriveBookCategoryDetailsData();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });



        //retrive category data


    }


    public void searchList(String query) {
        List<BookCategoryModel> filteredList = new ArrayList<>();
        String queryWithoutSpaces = query.replaceAll("\\s+", "").toLowerCase(); // Remove spaces from query

        for (BookCategoryModel obj : bookCategoryList) {
            String objStringWithoutSpaces = obj.toString().replaceAll("\\s+", "").toLowerCase(); // Remove spaces from object

            // Perform search based on bCategoryName without spaces and case-insensitive
            if (obj.getbCategoryName().replaceAll("\\s+", "").toLowerCase().contains(queryWithoutSpaces)) {
                filteredList.add(obj);
            }
        }

        // Update your UI with the filtered list
        bookCategoryAdapter.searchLists((ArrayList<BookCategoryModel>) filteredList);
        bookCategoryAdapter.notifyDataSetChanged();
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
                Collections.shuffle(bookCategoryList);
                bookCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}