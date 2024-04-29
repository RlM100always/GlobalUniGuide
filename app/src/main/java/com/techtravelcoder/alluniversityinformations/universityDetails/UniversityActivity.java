package com.techtravelcoder.alluniversityinformations.universityDetails;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.countryDetails.MainActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class UniversityActivity extends AppCompatActivity {


    private  RecyclerView recyclerView,recyclerView1,recyclerViewPub,recyclerViewPrv,recyclerViewSugg;
    private TextView cName,bestUni,allUni,pubUni,prvUni,moreUni,loadindUni,backPressed;
    private SwipeRefreshLayout swipeRefreshLayout;

    private UniversityAdapter campainAdapter;
    private UniversityAdapter1 campainAdapter1;
    private UniversityAdapterPub universityAdapterPub;
    private UniversityAdapterPrv universityAdapterPrv;
    private UniversityAdapterSugg universityAdapterSugg;


    private ArrayList<UniversityModel> list,list1,listPub,listPrv,listSugg;
    UniversityModel campainModel,campainModel1,campainModelPub,campainModelPrv,campainModelSugg;
    DatabaseReference mbase,mbase1,mbasePub,mbasePrv,mbaseSugg;
    SearchView searchView;
    private ProgressBar progressBar;

    String contryName;
    private String lastItemId;


    NestedScrollView nestedScrollView;



    //pagination
    private static final int PAGE_SIZE = 19;
    private static final int MAX_ITEMS_TO_LOAD = 500;
    private int totalLoadedItems = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);

        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.back);
        }
        getWindow().setStatusBarColor(color);



        nestedScrollView=findViewById(R.id.nested_id);
        cName=findViewById(R.id.uni_coununtry_name_id);
        searchView=findViewById(R.id.searchView_uni);
        bestUni=findViewById(R.id.best_uni_id);
        allUni=findViewById(R.id.all_uni_id);
        pubUni=findViewById(R.id.public_uni_id);
        prvUni=findViewById(R.id.private_uni_id);
        moreUni=findViewById(R.id.more_id);
        loadindUni=findViewById(R.id.loading_id);
        backPressed=findViewById(R.id.back_pressed_id);

        swipeRefreshLayout=findViewById(R.id.university_suffle_id);

        mbase = FirebaseDatabase.getInstance().getReference("University");
        mbase1 = FirebaseDatabase.getInstance().getReference("University");
        mbasePub=FirebaseDatabase.getInstance().getReference("University");
        mbasePrv=FirebaseDatabase.getInstance().getReference("University");
        mbaseSugg=FirebaseDatabase.getInstance().getReference("University");

        mbase.keepSynced(true);


        allUniversity();
        topUniversity();
        publicUniversity();
        privateUniversity();
        Random random = new Random();
        int num = random.nextInt(19);
        suggestedUniversity(num);





        //loadindUni.setShadowLayer(3f, 0f, 0f, Color.WHITE); // Adjust shadow radius as needed

        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.allert_back_upper));
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

        progressBar=findViewById(R.id.progressBar_uni);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                bestUni.setVisibility(View.GONE);
                allUni.setVisibility(View.GONE);
                pubUni.setVisibility(View.GONE);
                prvUni.setVisibility(View.GONE);
                bestUni.setVisibility(View.GONE);
                moreUni.setVisibility(View.GONE);
                backPressed.setVisibility(View.GONE);
                cName.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

//                recyclerViewPub.setVisibility(View.GONE);
//                recyclerViewPrv.setVisibility(View.GONE);
//                recyclerView1.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.GONE);
//                recyclerViewSugg.setVisibility(View.GONE);

                refreshData();
                // After refreshing, call setRefreshing(false) to stop the loading animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });



        contryName=getIntent().getStringExtra("name");

        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("val",2);
                startActivity(intent);
            }
        });

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                // User has scrolled to the bottom
                loadNextPage();

            }
        });





    }

//    public  class LoadUniversityDataTask extends AsyncTask<Void, Void, Integer> {
//        @Override
//        protected Integer doInBackground(Void... voids) {
//            allUniversity();
//            topUniversity();
//            publicUniversity();
//            privateUniversity();
//            Random random = new Random();
//            int num = random.nextInt(19);
//            suggestedUniversity(num);
//            return num;
//        }
//
//        @Override
//        protected void onPostExecute(Integer num) {
//            super.onPostExecute(num);
//            // Update UI components after data loading
//            //updateUI();
//        }
//    }



    private void loadNextPage() {
        if (totalLoadedItems >= MAX_ITEMS_TO_LOAD) {
            Toast.makeText(this, "No University found", Toast.LENGTH_SHORT).show();
            // Already loaded the maximum number of items
            return;
        }
        Query nextPageQuery = mbaseSugg.orderByKey().startAfter(lastItemId).limitToFirst(PAGE_SIZE);
        nextPageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UniversityModel universityModel = dataSnapshot.getValue(UniversityModel.class);
                        if (universityModel != null && universityModel.getContryName() != null && (universityModel.getSuggested().equals("true") || universityModel.getSuggested().equals("true "))) {
                            listSugg.add(universityModel);
                            lastItemId = universityModel.getKey();
                            totalLoadedItems++;
                            if(totalLoadedItems>=MAX_ITEMS_TO_LOAD){
                                break;
                            }
                        }
                    }

                    // Add new items to the existing list
                    universityAdapterSugg.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Log.e(TAG, "loadNextPage: onCancelled", error.toException());
            }
        });
    }
    private void suggestedUniversity(int num) {
        recyclerViewSugg=findViewById(R.id.more_suggested_university);
        recyclerViewSugg.clearOnScrollListeners();
        recyclerViewSugg.setLayoutManager(new LinearLayoutManager(this));
        listSugg=new ArrayList<>();
        universityAdapterSugg=new UniversityAdapterSugg(this,listSugg);
        recyclerViewSugg.setAdapter(universityAdapterSugg );

        Query query = null;
        if(num==0){
            query = mbaseSugg.limitToFirst(10);

        }
        else if(num==1){
             query = mbaseSugg.orderByChild("uniName")
                    .startAt("A").endAt("A\uf8ff");
        }
        else if(num==2){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("B").endAt("C\uf8ff");
        }
        else if(num==3){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("C").endAt("C\uf8ff");
        }
        else if(num==4){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("D").endAt("D\uf8ff");
        }
        else if(num==5){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("E").endAt("E\uf8ff");
        }
        else if(num==6){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("F").endAt("F\uf8ff");
        }
        else if(num==7){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("G").endAt("G\uf8ff");
        }
        else if(num==8){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("H").endAt("H\uf8ff");
        }
        else if(num==9){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("I").endAt("I\uf8ff");
        }
        else if(num==10){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("J").endAt("J\uf8ff");
        }
        else if(num==11){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("K").endAt("K\uf8ff");
        }
        else if(num==12){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("L").endAt("L\uf8ff");
        }
        else if(num==13){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("M").endAt("M\uf8ff");
        }
        else if(num==14){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("N").endAt("N\uf8ff");
        }else if(num==15){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("O").endAt("O\uf8ff");
        }else if(num==16){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("P").endAt("P\uf8ff");
        }
        else if(num==17){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("Q").endAt("Q\uf8ff");
        }
        else if(num==18){
            query = mbaseSugg.orderByChild("uniName")
                    .startAt("R").endAt("R\uf8ff");
        }



        if(query!=null){
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        List<UniversityModel> suggestedItems = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            UniversityModel universityModel = dataSnapshot.getValue(UniversityModel.class);
                            if (universityModel != null && universityModel.getContryName() != null
                                    && (universityModel.getSuggested().equals("true") || universityModel.getSuggested().equals("true "))) {
                                suggestedItems.add(universityModel);
                                lastItemId = universityModel.getKey();
                            }
                        }

                        // Replace the list with new suggested items
                        listSugg.clear();
                        listSugg.addAll(suggestedItems);
                        Collections.shuffle(listSugg);
                        recyclerViewSugg.setVisibility(View.VISIBLE);

                        universityAdapterSugg.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                    Log.e(TAG, "suggestedUniversity: onCancelled", error.toException());
                }
            });
        }



    }



    private void allUniversity() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE); // or any other operation
        }
        recyclerView=findViewById(R.id.recycler_view_university_id);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        list=new ArrayList<>();
        campainAdapter=new UniversityAdapter(this,list);
        recyclerView.setAdapter(campainAdapter);

        mbase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    if(snapshot.exists()){
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            campainModel = dataSnapshot.getValue(UniversityModel.class);
                            if(campainModel != null && campainModel.getContryName() != null && campainModel.getContryName().equals(contryName)){
                                list.add(0,campainModel);
                            }
                        }
                        // Update UI only after fetching and processing data
                        campainAdapter.notifyDataSetChanged();
                        Collections.shuffle(list);
                        progressBar.setVisibility(View.GONE);
                        loadindUni.setVisibility(View.GONE);

                        backPressed.setVisibility(View.VISIBLE);// Dismiss ProgressDialog after updating UI

                        bestUni.setVisibility(View.VISIBLE);
                        cName.setVisibility(View.VISIBLE);
                        allUni.setVisibility(View.VISIBLE);
                        moreUni.setVisibility(View.VISIBLE);

//                        recyclerView.setVisibility(View.VISIBLE);
//                        recyclerView1.setVisibility(View.VISIBLE);
//                        recyclerViewPrv.setVisibility(View.VISIBLE);
//                        recyclerViewPub.setVisibility(View.GONE);




                        cName.setText(""+contryName+" University"+" List ");
                        allUni.setText("All "+contryName+" University");
                    }
                    else {
                        backPressed.setVisibility(View.VISIBLE);// Dismiss ProgressDialog after updating UI
                        // Handle case when no data exists
                        progressBar.setVisibility(View.GONE); // Dismiss ProgressDialog after updating UI
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled event
                    progressBar.setVisibility(View.GONE); // Dismiss ProgressDialog after updating UI
                }
            });

    }

    private void searchList(String newText) {

        final long SEARCH_DELAY_MS = 300;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<UniversityModel> fListCampain = new ArrayList<>();
                        List<UniversityModel> fListCampain1 = new ArrayList<>();
                        List<UniversityModel> fListPub = new ArrayList<>();
                        List<UniversityModel> fListPrv = new ArrayList<>();
                        List<UniversityModel> fListSugg = new ArrayList<>();

                        for (UniversityModel obj : list) {
                            if (obj.getUniName().toLowerCase().contains(newText.toLowerCase())) {
                                fListCampain.add(obj);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                campainAdapter.searchList((ArrayList<UniversityModel>) fListCampain);

                            }
                        });

                        for (UniversityModel obj : list1) {
                            if (obj.getUniName().toLowerCase().contains(newText.toLowerCase())) {
                                fListCampain1.add(obj);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                campainAdapter1.searchList1((ArrayList<UniversityModel>) fListCampain1);

                            }
                        });


                        for (UniversityModel obj : listPub) {
                            if (obj.getUniName().toLowerCase().contains(newText.toLowerCase())) {
                                fListPub.add(obj);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                universityAdapterPub.searchListPub((ArrayList<UniversityModel>) fListPub);

                            }
                        });

                        for (UniversityModel obj : listPrv) {
                            if (obj.getUniName().toLowerCase().contains(newText.toLowerCase())) {
                                fListPrv.add(obj);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                universityAdapterPrv.searchListPrv((ArrayList<UniversityModel>) fListPrv);

                            }
                        });

                        for (UniversityModel obj : listSugg) {
                            if (obj.getUniName().toLowerCase().contains(newText.toLowerCase())) {
                                fListSugg.add(obj);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                universityAdapterSugg.searchListSugg((ArrayList<UniversityModel>) fListSugg);

                            }
                        });


                    }
                });

            }
        },SEARCH_DELAY_MS);

    }
    private void privateUniversity() {
        recyclerViewPrv=findViewById(R.id.private_recyclerview_id);
        recyclerViewPrv.setLayoutManager(new GridLayoutManager(this,1,GridLayoutManager.HORIZONTAL,false));
        listPrv=new ArrayList<>();
        universityAdapterPrv=new UniversityAdapterPrv(this,listPrv);
        recyclerViewPrv.setAdapter(universityAdapterPrv);
        mbasePrv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listPrv.clear();

                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        campainModelPrv = dataSnapshot.getValue(UniversityModel.class);
                        //&& campainModel.getCountryName().equals(contryName)


                        if(campainModelPrv != null  &&campainModelPrv.getContryName() != null&& campainModelPrv.getContryName().equals(contryName)
                                && (campainModelPrv.getPrivates().equals("true")|| campainModelPrv.getPublics().equals("true "))){
                            listPrv.add(0,campainModelPrv);

                        }

                    }

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Collections.shuffle(listPrv);
                        prvUni.setVisibility(View.VISIBLE);
                        prvUni.setText(contryName+" Private University");
                        universityAdapterPrv.notifyDataSetChanged();



                    }
                });


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void topUniversity() {
        recyclerView1=findViewById(R.id.top_recyclerview_id);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        list1=new ArrayList<>();
        campainAdapter1=new UniversityAdapter1(this,list1);
        recyclerView1.setAdapter(campainAdapter1);

        mbase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list1.clear();

                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        campainModel1 = dataSnapshot.getValue(UniversityModel.class);
                        //&& campainModel.getCountryName().equals(contryName)


                        if(campainModel1 != null  && campainModel1.getContryName() != null && campainModel1.getContryName().equals(contryName)
                                && (campainModel1.getBest().equals("true") || campainModel1.getPublics().equals("true "))){
                            list1.add(0,campainModel1);

                        }

                    }

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Collections.shuffle(list1);
                        bestUni.setText("Top Ranking "+contryName+" University");
                        campainAdapter1.notifyDataSetChanged();

                    }
                });


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void publicUniversity(){
        recyclerViewPub=findViewById(R.id.public_recyclerview_id);
        recyclerViewPub.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.HORIZONTAL,false));
        listPub=new ArrayList<>();
        universityAdapterPub=new UniversityAdapterPub(this,listPub);
        recyclerViewPub.setAdapter(universityAdapterPub);

        mbasePub.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    listPub.clear();

                    if(snapshot.exists()){
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            campainModelPub = dataSnapshot.getValue(UniversityModel.class);

                            if(campainModelPub != null  &&campainModelPub.getContryName() != null&& campainModelPub.getContryName().equals(contryName)
                                    && (campainModelPub.getPublics().equals("true") || campainModelPub.getPublics().equals("true "))){
                                listPub.add(0,campainModelPub);

                            }

                        }

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Collections.shuffle(listPub);
                            pubUni.setVisibility(View.VISIBLE);
                            pubUni.setText(contryName+" Public University");
                            universityAdapterPub.notifyDataSetChanged();



                        }
                    });


                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



    }

    private void refreshData() {

        allUniversity();
        topUniversity();
        publicUniversity();
        privateUniversity();
        Random random=new Random();
        int num=random.nextInt(19);
        suggestedUniversity(num);

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }


}