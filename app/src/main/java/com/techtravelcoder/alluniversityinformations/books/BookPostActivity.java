package com.techtravelcoder.alluniversityinformations.books;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.universityDetails.ReBookMarkAdapter;
import com.techtravelcoder.alluniversityinformations.universityDetails.UniversityModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private String bCataKey,categoryName;

    private ArrayList<BookPostModel> bookList;
    private BookPostAdapter bookPostAdapter;
    private ProgressBar progressBar;
    private TextView textView,condition;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_post);

        bCataKey=getIntent().getStringExtra("key");
        categoryName=getIntent().getStringExtra("category");




        //set statusbar color

        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.whiteTextSideColor1);
        }
        getWindow().setStatusBarColor(color);



        //initialize
        searchView=findViewById(R.id.book_post_search);
        recyclerView=findViewById(R.id.book_post_recyclerview_id);
        progressBar=findViewById(R.id.book_progressBar);
        textView=findViewById(R.id.book_text);
        imageView=findViewById(R.id.book_image);
        condition=findViewById(R.id.text_id);




        //progressbar color
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.whiteTextColor1), PorterDuff.Mode.SRC_IN);




        //searchview background
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.allert_back_upper));
        if(bCataKey.equals("@")){
            condition.setText("My BookMark Book List");
            retriveBookMarkBook();
        }
        else if(bCataKey.equals("@b")){
            condition.setText("Explore New Book");
            retriveNewbook();

        }else{
            condition.setText(categoryName+" Book");

            retriveBookDetailsData();
        }






    }


    private void retriveBookMarkBook() {
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        bookList=new ArrayList<>();
        bookPostAdapter=new BookPostAdapter(this,bookList,3);

        recyclerView.setAdapter(bookPostAdapter );
        FirebaseDatabase.getInstance().getReference("Book Details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        BookPostModel bookPostModel;

                        bookPostModel = dataSnapshot.getValue(BookPostModel.class);
                        if (bookPostModel != null) {
                            checkFavorite(bookPostModel);
                        }
                    }

                    bookPostAdapter.notifyDataSetChanged();

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkFavorite(BookPostModel bookPostModel) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseDatabase.getInstance().getReference("Book Details").child(bookPostModel.getBookKey())
                    .child("bookmark")
                    .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.exists()){
                                if ((snapshot.getValue(Boolean.class) != null) && (snapshot.getValue(Boolean.class))) {
                                    bookList.add(bookPostModel);

                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(bookList.size()==0){
                                            textView.setVisibility(View.VISIBLE);
                                            imageView.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);

                                        }else {
                                            textView.setVisibility(View.GONE);
                                            imageView.setVisibility(View.GONE);
                                            progressBar.setVisibility(View.GONE);

                                        }
                                    }
                                },1500);

                                bookPostAdapter.notifyDataSetChanged();


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }

    }

    private void retriveNewbook() {
        //start date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -10);
        Date startDate = calendar.getTime();

        bookList=new ArrayList<>();
        bookPostAdapter=new BookPostAdapter(BookPostActivity.this,bookList,2);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        recyclerView.setAdapter(bookPostAdapter);
        FirebaseDatabase.getInstance().getReference("Book Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                BookPostModel bookPostModel;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        bookPostModel = dataSnapshot.getValue(BookPostModel.class);
                        if (bookPostModel != null && isDateInRange(bookPostModel.getBookPostDate(),startDate)) {
                            bookList.add(0,bookPostModel);
                        }

                    }

                }
                bookPostAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if(bookList.size()==0){
                    textView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isDateInRange(String transactionDate, Date startDate) {
        // Assuming transactionDate is in the format "dd MMMM yyyy,EEEE"
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());

        try {
            Date date = sdf.parse(transactionDate);
            // Check if parsed date is after or equal to startDate
            return date.after(startDate) || date.equals(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void retriveBookDetailsData() {
        bookList=new ArrayList<>();
        bookPostAdapter=new BookPostAdapter(BookPostActivity.this,bookList,1);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        recyclerView.setAdapter(bookPostAdapter);
        FirebaseDatabase.getInstance().getReference("Book Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                BookPostModel bookPostModel;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        bookPostModel = dataSnapshot.getValue(BookPostModel.class);
                        if(bookPostModel != null){
                            if(bookPostModel.getBookCategoryKey().equals(bCataKey)){
                                bookList.add(0,bookPostModel);
                            }


                        }

                    }

                }
                bookPostAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if(bookList.size()==0){
                    textView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}