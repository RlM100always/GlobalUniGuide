package com.techtravelcoder.alluniversityinformations.Fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.techtravelcoder.alluniversityinformations.universityDetails.UniversityModel;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;


public class RecentFragment extends Fragment {

    private RecyclerView recyclerView;
    private MainPostAdapter mainPostAdapter;
    private ArrayList<MainPostModel> list;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private ImageView imageView;
    private TextView textView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public RecentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        mainPostAdapter = new MainPostAdapter(getContext(),list,1);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recent, container, false);

        imageView=view.findViewById(R.id.nothing_image);
        textView=view.findViewById(R.id.nothing_text_id);
        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout_recent);

        progressBar=view.findViewById(R.id.recent_progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.whiteTextColor1), PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.VISIBLE);

        mainPostAdapter.setViewTypeToShow(3);
        recyclerView = view.findViewById(R.id.recent_recyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(mainPostAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPostData();
            }
        });

        // Fetch data from Firebase Database
        fetchPostData();



        return  view;
    }

    private void fetchPostData() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -8);
        Date startDate = calendar.getTime();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MainPostModel mainPostModels= dataSnapshot.getValue(MainPostModel.class);
                    if (mainPostModels != null && isDateInRange(mainPostModels.getDate(),startDate)) {
                        list.add(0,mainPostModels);
                    }
                }


                Collections.sort(list, new Comparator<MainPostModel>() {
                    @Override
                    public int compare(MainPostModel u1, MainPostModel u2) {

                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());

                        try {
                            Date date2 = formatter.parse(u2.getDate());
                            Date date1 = formatter.parse(u1.getDate());
                            return date2.compareTo(date1);

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        // Sort by date in descending order
                    }
                });


                if(list.size()==0){
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                mainPostAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);

                    }
                },500);
                mainPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

}