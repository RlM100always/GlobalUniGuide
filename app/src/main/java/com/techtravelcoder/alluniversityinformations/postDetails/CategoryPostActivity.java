package com.techtravelcoder.alluniversityinformations.postDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.FragmentAdapter.MainPostAdapter;
import com.techtravelcoder.alluniversityinformations.FragmentModel.MainPostModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CategoryPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MainPostAdapter mainPostAdapter;
    private ArrayList<MainPostModel> list;
    private DatabaseReference databaseReference;
    private SearchView searchView;
    private TextView cat;
    String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_post);



        postId=getIntent().getStringExtra("id");

        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.back);
        }
        getWindow().setStatusBarColor(color);

        cat=findViewById(R.id.category_set_id);
        cat.setText(getIntent().getStringExtra("title"));

        searchView=findViewById(R.id.category_post_searchView);
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(ContextCompat.getColor(CategoryPostActivity.this, android.R.color.black));
        editText.setHintTextColor(ContextCompat.getColor(CategoryPostActivity.this, R.color.allert_back_upper));

        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        mainPostAdapter = new MainPostAdapter(CategoryPostActivity.this,list);

        int randomViewType = getRandomViewType();
        mainPostAdapter.setViewTypeToShow(randomViewType);
        recyclerView = findViewById(R.id.category_post_recyclerview_id);
        if(randomViewType==1){
            recyclerView.setLayoutManager(new LinearLayoutManager(CategoryPostActivity.this));
        } else if (randomViewType==2) {
            recyclerView.setLayoutManager(new GridLayoutManager(CategoryPostActivity.this,1));

        }
        recyclerView.setAdapter(mainPostAdapter);

        // Fetch data from Firebase Database
        fetchPostData();
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

    }

    public void  searchList(String query) {
        List<MainPostModel> filteredList = new ArrayList<>();
        String queryWithoutSpaces = query.replaceAll("\\s+", "").toLowerCase(); // Remove spaces from query

        for (MainPostModel obj : list) {
            String objStringWithoutSpaces = obj.toString().replaceAll("\\s+", "").toLowerCase(); // Remove spaces from object

            if (objStringWithoutSpaces.contains(queryWithoutSpaces)) {
                filteredList.add(obj);
            }
        }

        // Update your UI with the filtered list
        mainPostAdapter.searchLists((ArrayList<MainPostModel>) filteredList);
    }


    private void fetchPostData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MainPostModel mainPostModels= dataSnapshot.getValue(MainPostModel.class);
                    if (mainPostModels != null && mainPostModels.getUniqueNum().equals(postId)) {
                        list.add(mainPostModels);
                    }
                }
                mainPostAdapter.notifyDataSetChanged();
                Collections.shuffle(list);
                mainPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoryPostActivity.this, "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private int getRandomViewType() {
        Random random=new Random();
        int num=random.nextInt(2)+1;
        return num;
    }
}