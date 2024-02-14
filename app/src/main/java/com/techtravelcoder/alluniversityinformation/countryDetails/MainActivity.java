package com.techtravelcoder.alluniversityinformation.countryDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kotlin.Unit;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private CountryAdapter campainAdapter;
    private ArrayList<CountryModel> list;
    ArrayList<CountryModel> newlist;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseReference mbase;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    androidx.appcompat.widget.SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.back);
        }
        getWindow().setStatusBarColor(color);

        recyclerView=findViewById(R.id.main_recycler_id);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        drawerLayout=findViewById(R.id.drawer_id);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.tolbar);

        searchView=findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
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


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Drawable navIcon = toolbar.getNavigationIcon();
        if (navIcon != null) {
            navIcon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        }




        mbase = FirebaseDatabase.getInstance().getReference("Country");


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        list=new ArrayList<>();
        campainAdapter=new CountryAdapter(this,list);
        recyclerView.setAdapter(campainAdapter );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform data refresh operations (e.g., reload data from the server)

                // For example, you can reload your data or update the RecyclerView adapter here
                refreshData();

                // After refreshing, call setRefreshing(false) to stop the loading animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        loadData();

    }

    private void searchList(String newText) {
        List<CountryModel>fList=new ArrayList<>();
        for (CountryModel obj : list) {
            if (obj.getName().toLowerCase().contains(newText.toLowerCase())) {
                fList.add(obj);
            }

                campainAdapter.searchList((ArrayList<CountryModel>) fList);
        }

    }

    private void loadData() {
        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                CountryModel campainModel;
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        campainModel = dataSnapshot.getValue(CountryModel.class);
                        if (campainModel != null) {
                            list.add(0,campainModel);
                        }
                    }

                    campainAdapter.notifyDataSetChanged();
                    shuffleList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }private void refreshData() {
        loadData();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void shuffleList() {
        // Shuffle the list
        Collections.shuffle(list);

        // Notify the adapter after shuffling
        campainAdapter.notifyDataSetChanged();
    }

    private void setMenuCategoryColor(MenuItem menuItem, int textSizeInSp){
        SpannableString s = new SpannableString(menuItem.getTitle());
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        s.setSpan(new AbsoluteSizeSpan(textSizeInSp, true), 0, s.length(), 0);

        menuItem.setTitle(s);
    }
    public void navItemOnClickListener(){

        Menu menu=navigationView.getMenu();






        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                //https://www.youtube.com/@SelfMeTeam/videos


                return false;
            }
        });

    }
}