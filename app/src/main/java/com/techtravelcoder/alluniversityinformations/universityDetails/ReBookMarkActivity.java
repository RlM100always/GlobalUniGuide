package com.techtravelcoder.alluniversityinformations.universityDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.FragmentAdapter.MainPostAdapter;
import com.techtravelcoder.alluniversityinformations.FragmentModel.MainPostModel;
import com.techtravelcoder.alluniversityinformations.countryDetails.CountryAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReBookMarkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReBookMarkAdapter reBookMarkAdapter;
    private ArrayList<UniversityModel> list;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private ImageView imageView;
    private TextView textView,category;
    private androidx.appcompat.widget.SearchView searchView;
    private int check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_book_mark);



        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.back);
        }
        getWindow().setStatusBarColor(color);

        check=getIntent().getIntExtra("check",0);
        recyclerView=findViewById(R.id.rebookmark_recycler_id);
        imageView=findViewById(R.id.re_book_image);
        textView=findViewById(R.id.re_book_text_id);
        progressBar=findViewById(R.id.rebook_progressBar);
        category=findViewById(R.id.set_category_id);
        searchView=findViewById(R.id.rebook_searchView);
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.allert_back_upper));
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        // Initialize FirebaseAuth instance

        databaseReference = FirebaseDatabase.getInstance().getReference("University");
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });




        if(check==1){
            category.setText("New University List");
            fetchPostData();

        }
        if(check==2){
            category.setText("Your BookMark University List");
            bookMarkFetchPostData();

        }
    }

    private void fetchPostData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        reBookMarkAdapter=new ReBookMarkAdapter(this,list);
        reBookMarkAdapter.setViewTypeToShow(1);


        recyclerView.setAdapter(reBookMarkAdapter );
        progressBar.setVisibility(View.VISIBLE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date startDate = calendar.getTime();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UniversityModel universityModel= dataSnapshot.getValue(UniversityModel.class);
                    if(universityModel.getDate()!=null && universityModel != null){
                        if (  isDateInRange(universityModel.getDate(),startDate)) {
                            list.add(0,universityModel);
                        }
                    }

                }
                // Sort the list by date in descending order
                Collections.sort(list, new Comparator<UniversityModel>() {
                    @Override
                    public int compare(UniversityModel u1, UniversityModel u2) {
                        // Sort by date in descending order
                        return u2.getDate().compareTo(u1.getDate());
                    }
                });
                if(list.size()==0){
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
                reBookMarkAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReBookMarkActivity.this, "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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


    private void bookMarkFetchPostData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        reBookMarkAdapter=new ReBookMarkAdapter(this,list);
        reBookMarkAdapter.setViewTypeToShow(2);


        recyclerView.setAdapter(reBookMarkAdapter );
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UniversityModel universityModel = dataSnapshot.getValue(UniversityModel.class);
                        if (universityModel != null) {
                            checkFavorite(universityModel);
                        }
                    }

                    reBookMarkAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    if(list.size()==0){
                        textView.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                    }else {
                        textView.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                    }

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkFavorite(UniversityModel universityModel) {
        String key = universityModel.getKey();
        databaseReference.child(key)
                .child("favorite")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            if ((snapshot.getValue(Boolean.class) != null) && (snapshot.getValue(Boolean.class))) {
                                list.add(universityModel);

                            }
                            if(list.size()==0){
                                textView.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.VISIBLE);
                            }else {
                                textView.setVisibility(View.GONE);
                                imageView.setVisibility(View.GONE);
                            }
                            reBookMarkAdapter.notifyDataSetChanged();


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FavoriteFragment", "Failed to check favorite status: " + error.getMessage());
                    }
                });
    }

    public void  searchList(String query) {
        List<UniversityModel> filteredList = new ArrayList<>();
        String queryWithoutSpaces = query.replaceAll("\\s+", "").toLowerCase();

        for (UniversityModel obj : list) {
            String objStringWithoutSpaces = obj.toString().replaceAll("\\s+", "").toLowerCase(); // Remove spaces from object

            if (objStringWithoutSpaces.contains(queryWithoutSpaces)) {
                filteredList.add(obj);
            }
        }

        // Update your UI with the filtered list
        reBookMarkAdapter.searchLists((ArrayList<UniversityModel>) filteredList);
    }



}