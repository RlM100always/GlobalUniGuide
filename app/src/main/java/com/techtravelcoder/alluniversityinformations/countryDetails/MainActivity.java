package com.techtravelcoder.alluniversityinformations.countryDetails;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.universityDetails.UniversityModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private CountryAdapter campainAdapter;
    private ArrayList<CountryModel> list;
    ArrayList<CountryModel> newlist;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseReference mbase;
    private DrawerLayout drawerLayout;
    ProgressBar progressBar;
    NestedScrollView nestedScrollView;
    private Toolbar toolbar;
    private StartAppAd startAppAd = new StartAppAd(this);
    Banner banner;
    NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ProgressDialog progressDialog,progressDialog1;
    SearchView searchView;
    private LinearLayoutManager manager ;
    private AdView adView;
    CountryModel campainModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.back);
        }
        getWindow().setStatusBarColor(color);

        progressBar=findViewById(R.id.progressBar);
        manager = new LinearLayoutManager(this);
        recyclerView=findViewById(R.id.main_recycler_id);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        drawerLayout=findViewById(R.id.drawer_id);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.tolbar);
        progressDialog1=new ProgressDialog(MainActivity.this);


        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("✔✔ Loading ℹ️ℹ️");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("✔✔ Please Wait Country Information is loading ️ ️‼️");
        progressDialog.show();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.alert_back);
        progressDialog.getWindow().setBackgroundDrawable(drawable);





        banner=findViewById(R.id.banner_container);
        banner.loadAd();

//        adView = new AdView(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);
//        banner.addView(adView);
//        adView.loadAd();


        //iron source

//        IronSource.init(this, "1d901cc25");
//        IronSource.loadInterstitial();
//        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
//
//            @Override
//            public void onAdReady(AdInfo adInfo) {}
//            // Indicates that the ad failed to be loaded
//            @Override
//            public void onAdLoadFailed(IronSourceError error) {
//            }
//
//            @Override
//            public void onAdOpened(AdInfo adInfo) {}
//            @Override
//            public void onAdClosed(AdInfo adInfo) {}
//            // Invoked when the ad failed to show
//            @Override
//            public void onAdShowFailed(IronSourceError error, AdInfo adInfo) {}
//            // Invoked when end user clicked on the interstitial ad
//            @Override
//            public void onAdClicked(AdInfo adInfo) {}
//
//            @Override
//            public void onAdShowSucceeded(AdInfo adInfo){}
//        });



        FirebaseMessaging.getInstance().subscribeToTopic("News").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();
                String msg="Done";
                if(!task.isSuccessful()){
                    msg="Failed";
                }


            }
        });

        searchView=findViewById(R.id.searchView);
       // searchView.clearFocus();
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
        loadData();



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.setVisibility(View.GONE);
                progressDialog1.setTitle("ℹ️ℹ️ Refreshing ");
                progressDialog1.setCancelable(false);
                progressDialog1.setMessage("✔✔ Please Wait Country Information is refreshing ‼️");
                progressDialog1.show();
                Drawable drawable1= ContextCompat.getDrawable(getApplicationContext(),R.drawable.alert_back);
                progressDialog1.getWindow().setBackgroundDrawable(drawable1);
                loadData();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        navItemOnClickListener();

    }

    private void searchList(String newText) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                List<CountryModel>fList=new ArrayList<>();
                for (CountryModel obj : list) {
                  //  Toast.makeText(getApplicationContext(), ""+obj.getName()+" "+list.size(), Toast.LENGTH_SHORT).show();

                    if (obj.getName().toLowerCase().contains(newText.toLowerCase())) {
                        fList.add(obj);
                    }

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        campainAdapter.searchLists((ArrayList<CountryModel>) fList);

                    }
                });

            }
        },10);


    }


    private void loadData() {


        mbase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                list.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        campainModel = dataSnapshot.getValue(CountryModel.class);
                        if (campainModel != null) {
                            list.add(0,campainModel);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            progressDialog1.dismiss();
                            recyclerView.setVisibility(View.VISIBLE);


                            //shuffleList();
                            campainAdapter.notifyDataSetChanged();
                            // Notify the adapter after shuffling
                            Collections.shuffle(list);

                            campainAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void navItemOnClickListener(){


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                //https://www.youtube.com/@SelfMeTeam/videos
                //https://github.com/RlM100always/uni_info_privacy_policy/blob/main/README.md

                if(item.getItemId()==R.id.menu_privacy_id){

                    String websiteUrl = "https://github.com/RlM100always/uni_info_privacy_policy/blob/main/README.md";
                    Intent openWebsiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                    startActivity(openWebsiteIntent);

                }
                if(item.getItemId()==R.id.menu_contact_id){
                    String[] recipientEmails = {"selfmeteam@gmail.com"};

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, recipientEmails);
                    startActivity(emailIntent);

//                    if (emailIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivity(emailIntent);
//                    }
                }
                if(item.getItemId()==R.id.menu_developer_id){
                    //https://www.facebook.com/profile.php?id=61550636764055

                    String websiteUrl = "https://www.facebook.com/profile.php?id=61550636764055";
                    Intent openWebsiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                    startActivity(openWebsiteIntent);
                }
                if(item.getItemId()==R.id.menu_rate_id){
                    Uri uri=Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());

                    Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                    try {
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Unable to ratting !!!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                if(item.getItemId()==R.id.menu_share_id){

                    try {
                        Intent intent= new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT,"✅✅ Best Higher Studies And University Guidelines App");
                        intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());

                        startActivity(Intent.createChooser(intent,"Share With"));
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Unable to Share!!!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder alertObj= new AlertDialog.Builder(MainActivity.this);
        alertObj.setTitle("Confirm Exit...ℹ️");
        alertObj.setMessage("ℹ️ Do you want to Exit this Application ❓❓");

        alertObj.setPositiveButton("✅Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alertObj.setNegativeButton("❌No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = alertObj.create();
        dialog.show();

        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.alert_back);
        dialog.getWindow().setBackgroundDrawable(drawable);



    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}