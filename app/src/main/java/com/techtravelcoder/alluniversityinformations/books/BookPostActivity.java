package com.techtravelcoder.alluniversityinformations.books;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private String bCataKey,categoryName;

    private ArrayList<BookModel> bookList;
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
            color = getColor(R.color.back);
        }
        getWindow().setStatusBarColor(color);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);




        //initialize
        searchView=findViewById(R.id.book_post_search);
        recyclerView=findViewById(R.id.book_post_recyclerview_id);
        progressBar=findViewById(R.id.book_progressBar);
        textView=findViewById(R.id.book_text);
        imageView=findViewById(R.id.book_image);
        condition=findViewById(R.id.text_id);


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

    public void searchList(String query) {
        List<BookModel> filteredList = new ArrayList<>();
        String queryWithoutSpaces = query.replaceAll("[/><:{}`+=*.||?()$#%!\\-,@&_\\n\\s]", "").toLowerCase();

        for (BookModel obj : bookList) {
            String objStringWithoutSpaces = obj.toString().replaceAll("[/><:{}`+=*.||?()$#%!\\-,@&_\\n\\s]", "").toLowerCase(); // Remove spaces from object

            // Perform search based on bCategoryName without spaces and case-insensitive
            if (obj.getBookName().replaceAll("\\s+", "").toLowerCase().contains(queryWithoutSpaces)) {
                filteredList.add(obj);
            }
        }

        // Update your UI with the filtered list
        bookPostAdapter.searchLists((ArrayList<BookModel>) filteredList);
        bookPostAdapter .notifyDataSetChanged();
    }



    private void retriveBookMarkBook() {
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        bookList=new ArrayList<>();
        bookPostAdapter=new BookPostAdapter(this,bookList,3);

        recyclerView.setAdapter(bookPostAdapter );
        FirebaseDatabase.getInstance().getReference("Book Details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        BookModel bookPostModel;

                        bookPostModel = dataSnapshot.getValue(BookModel.class);
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

    private void checkFavorite(BookModel bookPostModel) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            // Access the specific user's bookmark node
            FirebaseDatabase.getInstance().getReference("Book Details")
                    .child(bookPostModel.getBookKey())
                    .child("bookmarkbook")
                    .child(FirebaseAuth.getInstance().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Retrieve the bookmark details
                                Bookmark bookmark1 = snapshot.getValue(Bookmark.class);

                                // Check if the bookmark exists
                                if (bookmark1 != null && bookmark1.getUserBookmark()==true) {
                                    if(bookList.size()<180){
                                        bookList.add(bookPostModel);
                                    }

                                }

                                Collections.sort(bookList, new Comparator<BookModel>() {
                                    @Override
                                    public int compare(BookModel book1, BookModel book2) {
                                        Long time1 = getLatestBookmarkTime(book1);
                                        Long time2 = getLatestBookmarkTime(book2);
                                        return time2.compareTo(time1); // Descending order
                                    }
                                });

                                if(!bookList.isEmpty()){
                                    progressBar.setVisibility(View.GONE);
                                    bookPostAdapter.notifyDataSetChanged();
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(bookList.isEmpty()){
                                            imageView.setVisibility(View.VISIBLE);
                                            textView.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);
                                        }else {
                                            imageView.setVisibility(View.GONE);
                                            textView.setVisibility(View.GONE);
                                        }

                                    }
                                }, 2000);
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                            Log.e("Tag", error.getMessage());
                            Toast.makeText(BookPostActivity.this, "Error fetching bookmark: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private Long getLatestBookmarkTime(BookModel bookPostModel) {
        Map<String, Bookmark> bookmarks = bookPostModel.getBookmarkbook();
        Long latestTime = null;

        // Iterate through the bookmarks to find the latest time
        for (Map.Entry<String, Bookmark> entry : bookmarks.entrySet()) {
            Bookmark bookmark = entry.getValue();
            if (bookmark != null && bookmark.getTime() != null) {
                Long time = bookmark.getTime(); // Assuming getTime() returns Long
                if (latestTime == null || time > latestTime) {
                    latestTime = time;
                }
            }
        }
        return latestTime;
    }

    private void retriveNewbook() {
        //start date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -10);
        Date startDate = calendar.getTime();

        bookList=new ArrayList<>();
        bookPostAdapter=new BookPostAdapter(BookPostActivity.this,bookList,2);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView.setAdapter(bookPostAdapter);
        FirebaseDatabase.getInstance().getReference("Book Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                BookModel bookPostModel;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        bookPostModel = dataSnapshot.getValue(BookModel.class);
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
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView.setAdapter(bookPostAdapter);
        FirebaseDatabase.getInstance().getReference("Book Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                BookModel bookPostModel;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        bookPostModel = dataSnapshot.getValue(BookModel.class);
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