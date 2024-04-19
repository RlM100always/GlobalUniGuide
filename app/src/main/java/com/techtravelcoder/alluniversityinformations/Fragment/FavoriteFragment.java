package com.techtravelcoder.alluniversityinformations.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.FragmentAdapter.MainPostAdapter;
import com.techtravelcoder.alluniversityinformations.FragmentModel.MainPostModel;
import com.techtravelcoder.alluniversityinformations.postDetails.PostWebViewActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FavoriteFragment extends Fragment {


    private RecyclerView recyclerView;
    private MainPostAdapter mainPostAdapter;
    private ArrayList<MainPostModel> list;
    private DatabaseReference databaseReference;
    private ImageView imageView;
    private TextView textView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    public FavoriteFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        mainPostAdapter = new MainPostAdapter(getContext(),list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        textView=view.findViewById(R.id.favorite_text_id);
        imageView=view.findViewById(R.id.favorite_image);
        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout_favorite);

        progressBar=view.findViewById(R.id.favorite_progressBar);
        mainPostAdapter.setViewTypeToShow(4);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPostData();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        recyclerView = view.findViewById(R.id.favorite_recyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(mainPostAdapter);

        fetchPostData();
        return view;
    }

    private void fetchPostData() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        MainPostModel mainPostModel = dataSnapshot.getValue(MainPostModel.class);
                        if (mainPostModel != null) {
                            checkFavorite(mainPostModel);
                        }
                    }

                    mainPostAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkFavorite(MainPostModel mainPostModel) {
        String key = mainPostModel.getKey();
                databaseReference.child(key)
                .child("favorite")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    if ((snapshot.getValue(Boolean.class) != null) && (snapshot.getValue(Boolean.class))) {
                        list.add(mainPostModel);
                        mainPostAdapter.notifyDataSetChanged();

                    }
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
                Log.e("FavoriteFragment", "Failed to check favorite status: " + error.getMessage());
            }
        });
    }


}