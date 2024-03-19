package com.techtravelcoder.alluniversityinformations.universityDetails;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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


    RecyclerView recyclerView,recyclerView1,recyclerViewPub,recyclerViewPrv,recyclerViewSugg;
    TextView cName,bestUni,allUni,pubUni,prvUni,moreUni;
    SwipeRefreshLayout swipeRefreshLayout;

    private UniversityAdapter campainAdapter;
    private UniversityAdapter1 campainAdapter1;
    private UniversityAdapterPub universityAdapterPub;
    private UniversityAdapterPrv universityAdapterPrv;
    private UniversityAdapterSugg universityAdapterSugg;


    private ArrayList<UniversityModel> list,list1,listPub,listPrv,listSugg;
    UniversityModel campainModel,campainModel1,campainModelPub,campainModelPrv,campainModelSugg;
    DatabaseReference mbase,mbase1,mbasePub,mbasePrv,mbaseSugg;
    SearchView searchView;

    String contryName;
    private Banner banner ;
    private StartAppAd startAppAd = new StartAppAd(this);
    private AdView adView;
     ProgressDialog progressDialog,progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);

        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.back);
        }
        getWindow().setStatusBarColor(color);

        banner=findViewById(R.id.banner_container_uni);
        banner.loadAd();

//        adView = new AdView(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);
//        banner.addView(adView);
//        adView.loadAd();

        progressDialog1=new ProgressDialog(UniversityActivity.this);

        cName=findViewById(R.id.uni_coununtry_name_id);
        searchView=findViewById(R.id.searchView_uni);
        bestUni=findViewById(R.id.best_uni_id);
        allUni=findViewById(R.id.all_uni_id);
        pubUni=findViewById(R.id.public_uni_id);
        prvUni=findViewById(R.id.private_uni_id);
        moreUni=findViewById(R.id.more_id);
        swipeRefreshLayout=findViewById(R.id.university_suffle_id);
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                progressDialog1.setTitle("ℹ️ℹ️ Refreshing ");
                progressDialog1.setCancelable(false);
                progressDialog1.setMessage("✔✔ Please Wait University Information is refreshing ‼️");
                progressDialog1.show();
                Drawable drawable1= ContextCompat.getDrawable(getApplicationContext(),R.drawable.alert_back);
                progressDialog1.getWindow().setBackgroundDrawable(drawable1);

                bestUni.setVisibility(View.GONE);
                allUni.setVisibility(View.GONE);
                pubUni.setVisibility(View.GONE);
                prvUni.setVisibility(View.GONE);
                bestUni.setVisibility(View.GONE);
                moreUni.setVisibility(View.GONE);

                refreshData();
                // After refreshing, call setRefreshing(false) to stop the loading animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        contryName=getIntent().getStringExtra("name");
        cName.setText(""+contryName+" University");
        bestUni.setText("Top Ranking "+contryName+" University");
        allUni.setText("All "+contryName+" University");
        pubUni.setText(contryName+" Public University");
        prvUni.setText(contryName+" Private University");

        mbase = FirebaseDatabase.getInstance().getReference("University");
        mbase1 = FirebaseDatabase.getInstance().getReference("University");
        mbasePub=FirebaseDatabase.getInstance().getReference("University");
        mbasePrv=FirebaseDatabase.getInstance().getReference("University");
        mbaseSugg=FirebaseDatabase.getInstance().getReference("University");

        progressDialog=new ProgressDialog(UniversityActivity.this);
        progressDialog.setTitle("✔✔ Loading ℹ️ℹ️");
        progressDialog.setMessage("✔✔ Please Wait University Information is loading‼️");
        progressDialog.setProgressPercentFormat(NumberFormat.getPercentInstance());
        progressDialog.setCancelable(false);

        progressDialog.show();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.alert_back);
        progressDialog.getWindow().setBackgroundDrawable(drawable);




        Runnable topRunnable = () -> topUniversity();
        Runnable publicRunnable = () -> publicUniversity();
        Runnable privateRunnable = () -> privateUniversity();
        Runnable suggestedRunnable = () -> suggestedUniversity();
        Runnable allRunnable = () -> allUniversity();


        Thread topThread = new Thread(topRunnable);
        Thread publicThread = new Thread(publicRunnable);
        Thread privateThread = new Thread(privateRunnable);
        Thread suggestedThread = new Thread(suggestedRunnable);
        Thread allThread = new Thread(allRunnable);

//
        topThread.start();
        publicThread.start();
        privateThread.start();
        suggestedThread.start();
        allThread.start();

//        publicUniversity();
//        privateUniversity();
//        allUniversity();
//        suggestedUniversity();


    }

    private void suggestedUniversity() {


        recyclerViewSugg=findViewById(R.id.more_suggested_university);
        recyclerViewSugg.setLayoutManager(new GridLayoutManager(this, 1));
        listSugg=new ArrayList<>();
        universityAdapterSugg=new UniversityAdapterSugg(this,listSugg);
        recyclerViewSugg.setAdapter(universityAdapterSugg );
        mbaseSugg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listSugg.clear();

                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        campainModelSugg = dataSnapshot.getValue(UniversityModel.class);

                        if(campainModelSugg != null  &&campainModelSugg.getContryName() != null
                        && (campainModelSugg.getSuggested().equals("true") || campainModelSugg.getPublics().equals("true "))){
                            listSugg.add(0,campainModelSugg);
                        }

                    }

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        universityAdapterSugg.notifyDataSetChanged();
                        Collections.shuffle(listSugg);
                        universityAdapterSugg.notifyDataSetChanged();
                    }
                });





            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void allUniversity(){
        recyclerView=findViewById(R.id.recycler_view_university_id);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        list=new ArrayList<>();
        campainAdapter=new UniversityAdapter(this,list);
        recyclerView.setAdapter(campainAdapter );
        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        campainModel = dataSnapshot.getValue(UniversityModel.class);

                        if(campainModel != null  &&campainModel.getContryName() != null&& campainModel.getContryName().equals(contryName)){
                            list.add(0,campainModel);
                        }

                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            progressDialog1.dismiss();

                            // Show other views if needed
                            bestUni.setVisibility(View.VISIBLE);
                            allUni.setVisibility(View.VISIBLE);
                            pubUni.setVisibility(View.VISIBLE);
                            prvUni.setVisibility(View.VISIBLE);
                            bestUni.setVisibility(View.VISIBLE);
                            moreUni.setVisibility(View.VISIBLE);
                        }
                    });


                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        campainAdapter.notifyDataSetChanged();
                        Collections.shuffle(list);
                        campainAdapter.notifyDataSetChanged();
                    }
                });



            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        universityAdapterPrv.notifyDataSetChanged();
                        Collections.shuffle(listPrv);
                        campainAdapter.notifyDataSetChanged();
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
                        campainAdapter1.notifyDataSetChanged();
                        Collections.shuffle(list1);
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
                        //&& campainModel.getCountryName().equals(contryName)


                        if(campainModelPub != null  &&campainModelPub.getContryName() != null&& campainModelPub.getContryName().equals(contryName)
                        && (campainModelPub.getPublics().equals("true") || campainModelPub.getPublics().equals("true "))){
                            listPub.add(0,campainModelPub);

                        }

                    }

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        universityAdapterPub.notifyDataSetChanged();
                        Collections.shuffle(listPub);
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
        privateUniversity();
        publicUniversity();
        topUniversity();
        suggestedUniversity();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        Random random=new Random();
        int num=random.nextInt(3)+1;
        if(num==2){
            startAppAd.onBackPressed();
        }
        super.onBackPressed();
    }
}