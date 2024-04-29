package com.techtravelcoder.alluniversityinformations.Fragment;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.FragmentAdapter.CategoryAdapter;
import com.techtravelcoder.alluniversityinformations.FragmentAdapter.MainPostAdapter;
import com.techtravelcoder.alluniversityinformations.FragmentModel.CategoryModel;
import com.techtravelcoder.alluniversityinformations.FragmentModel.MainPostModel;
import com.techtravelcoder.alluniversityinformations.postDetails.PostHandleActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment   {


    private RecyclerView recyclerView;
    private MainPostAdapter mainPostAdapter;
    private ArrayList<MainPostModel>list;
    private DatabaseReference databaseReference;
    private SearchView searchView;
    private ProgressBar progressBar;

    //pagination
    private static final int PAGE_SIZE =10;
    private int totalLoadedItems = 0;
    private String lastItemId;
    private NestedScrollView scrollView;
    private Query query;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private int randomViewType;




    public HomeFragment() {
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        swipeRefreshLayout=view.findViewById(R.id.home_swipe_refresh_layout);

        scrollView=view.findViewById(R.id.home_frag_scroll_id);
        progressBar=view.findViewById(R.id.home_progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.home_recyclerview_id);




        //searchview
        searchView=view.findViewById(R.id.home_searchView);
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        editText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.allert_back_upper));






        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                // User has scrolled to the bottom
               fetchPaginateData();

            }
        });


        ;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Random random=new Random();
                int num=random.nextInt(7);
                fetchPostData(num);

            }
        });

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

        Random random=new Random();
        int num=random.nextInt(7);
        fetchPostData(num);



        return view;
    }


    private void fetchPaginateData() {

        Query nextPageQuery = databaseReference.orderByKey().startAfter(lastItemId).limitToFirst(PAGE_SIZE);

        nextPageQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MainPostModel mainPostModels= dataSnapshot.getValue(MainPostModel.class);
                    if (mainPostModels != null) {
                        list.add(mainPostModels);
                        lastItemId = mainPostModels.getKey();
                        totalLoadedItems++;

                    }
                }
                //Toast.makeText(getContext(), ""+list.size(), Toast.LENGTH_SHORT).show();

                mainPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void fetchPostData(int num) {
        list = new ArrayList<>();
        mainPostAdapter = new MainPostAdapter(getContext(),list);
        randomViewType = getRandomViewType();
        mainPostAdapter.setViewTypeToShow(randomViewType);
        if(randomViewType==1){
            recyclerView.setLayoutManager(linearLayoutManager);
        } else if (randomViewType==2) {
            recyclerView.setLayoutManager(linearLayoutManager);

        }
        recyclerView.setAdapter(mainPostAdapter);

        if(num==0){
            query=databaseReference.limitToFirst(15);
        }else if(num==1){
            query = databaseReference.orderByChild("title")
                    .startAt("A").endAt("A\uf8ff");
        }
        else if(num==2){
            query = databaseReference.orderByChild("title")
                    .startAt("B").endAt("C\uf8ff");
        }
        else if(num==3){
            query = databaseReference.orderByChild("title")
                    .startAt("C").endAt("C\uf8ff");
        }

        else if(num==4){
            query = databaseReference.orderByChild("title")
                    .startAt("H").endAt("H\uf8ff");
        }
        else if(num==5){
            query = databaseReference.orderByChild("title")
                    .startAt("M").endAt("M\uf8ff");
        }
        else if(num==6){
            query = databaseReference.orderByChild("title")
                    .startAt("P").endAt("P\uf8ff");
        }


        if(query!=null){
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        MainPostModel mainPostModels= dataSnapshot.getValue(MainPostModel.class);
                        if (mainPostModels != null) {
                            list.add(mainPostModels);
                            lastItemId = mainPostModels.getKey();

                        }
                    }
                  //  Toast.makeText(getContext(), ""+lastItemId, Toast.LENGTH_SHORT).show();

                    mainPostAdapter.notifyDataSetChanged();
                    Collections.shuffle(list);
                    progressBar.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    },1000);
                    mainPostAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private int getRandomViewType() {
        Random random=new Random();
        int num=random.nextInt(2)+1;
        return num;
    }


}