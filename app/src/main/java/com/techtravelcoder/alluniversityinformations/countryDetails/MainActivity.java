package com.techtravelcoder.alluniversityinformations.countryDetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.onesignal.notifications.INotificationClickEvent;
import com.onesignal.notifications.INotificationClickListener;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.ads.ADSSetUp;
import com.techtravelcoder.alluniversityinformations.ads.App;
import com.techtravelcoder.alluniversityinformations.postDetails.CategoryPostActivity;
import com.techtravelcoder.alluniversityinformations.postDetails.PostHandleActivity;
import com.techtravelcoder.alluniversityinformations.universityDetails.ReBookMarkActivity;
import com.techtravelcoder.alluniversityinformations.vocabulary.VocabularyActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import co.notix.interstitial.NotixInterstitial;
import kotlin.Unit;

public class MainActivity extends AppCompatActivity  {

    RecyclerView recyclerView;
    private CountryAdapter campainAdapter;
    private ArrayList<CountryModel> list;
    ArrayList<CountryModel> newlist;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseReference mbase;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    Banner banner;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SearchView searchView;

    private CountryModel campainModel;
    private static final int PAGE_SIZE = 20;

    private GridLayoutManager gridLayoutManager;
    private ProgressBar progressBar;
    private LinearLayout visit,recentUni,carrierGuide,popularContent,bookUni;
    private static final String ONESIGNAL_APP_ID = "4966cbfa-9bdd-4424-9b60-b11fd884cee5";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //for ads disable
        StartAppSDK.init(this, "201407686");
        StartAppAd.disableAutoInterstitial();


        //Toast.makeText(this, ""+OneSignal.getSession(), Toast.LENGTH_SHORT).show();
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);
        OneSignal.getNotifications().addClickListener(new INotificationClickListener() {
            @Override
            public void onClick(@NonNull INotificationClickEvent iNotificationClickEvent) {
                iNotificationClickEvent.getNotification().getTemplateName();
                Toast.makeText(MainActivity.this, ""+iNotificationClickEvent.getNotification().getTemplateName(), Toast.LENGTH_SHORT).show();
                //Log.d("Value",iNotificationClickEvent.getNotification().getTemplateName());
                if(iNotificationClickEvent.getNotification().getTemplateName().equals("University")){
                    Intent intent=new Intent(MainActivity.this, ReBookMarkActivity.class);
                    intent.putExtra("check",1);
                    startActivity(intent);
                }
                if(iNotificationClickEvent.getNotification().getTemplateName().equals("Popular Content")){
                    Intent intent=new Intent(MainActivity.this, ReBookMarkActivity.class);
                    intent.putExtra("check",2);
                    startActivity(intent);
                }

            }
        });






          if (FirebaseAuth.getInstance().getCurrentUser() != null) {

          } else {
            signInAnonymously();
          }


//        App.appOpenLoader.doOnNextAvailable(result -> {
//            if (result != null) {
//                NotixAppOpen.Companion.show(result);
//            }
//            return Unit.INSTANCE;
//        });



        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.back);
        }
        getWindow().setStatusBarColor(color);

        gridLayoutManager=new GridLayoutManager(MainActivity.this,2);
        progressBar=findViewById(R.id.progressBar_id);
        recyclerView=findViewById(R.id.main_recycler_id);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        drawerLayout=findViewById(R.id.drawer_id);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.tolbar);
        recentUni=findViewById(R.id.recent_uni_id);
        carrierGuide=findViewById(R.id.carrier_guide_id);
        popularContent=findViewById(R.id.popular_content_id);
        bookUni=findViewById(R.id.bookmark_uni_id);

        // Initialize FirebaseAuth instance

        mbase = FirebaseDatabase.getInstance().getReference("Country");
        mbase.keepSynced(true);




        progressBar.setVisibility(View.VISIBLE);
        loadData();
        refeshData();


        recentUni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ReBookMarkActivity.class);
                ADSSetUp.adsType1(MainActivity.this);
                intent.putExtra("check",1);
                startActivity(intent);
            }
        });
        carrierGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, PostHandleActivity.class);
                ADSSetUp.adsType1(MainActivity.this);
                startActivity(intent);
            }
        });
        popularContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, CategoryPostActivity.class);
                ADSSetUp.adsType1(MainActivity.this);
                intent.putExtra("title","Most Popular Content");
                startActivity(intent);
            }
        });
        bookUni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ReBookMarkActivity.class);
                ADSSetUp.adsType1(MainActivity.this);
                intent.putExtra("check",2);
                startActivity(intent);
            }
        });

        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        FirebaseMessaging.getInstance().subscribeToTopic("News").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg="Done";
                if(!task.isSuccessful()){
                    msg="Failed";
                }


            }
        });

        searchView=findViewById(R.id.searchView);

        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.allert_back_upper));

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


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refeshData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        navItemOnClickListener();




    }

    private void signInAnonymously() {
        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Successfully Load.", Toast.LENGTH_SHORT).show();

                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(MainActivity.this, "An error occurred: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void refeshData() {
        recyclerView.setLayoutManager(gridLayoutManager);
        list=new ArrayList<>();
        campainAdapter=new CountryAdapter(this,list);
        recyclerView.setAdapter(campainAdapter );
        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        campainModel = dataSnapshot.getValue(CountryModel.class);
                        if (campainModel != null) {
                            list.add(campainModel);
                        }
                    }
                    Collections.shuffle(list);
                    campainAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

    }


    private HashSet<String> uniqueKeys = new HashSet<>();

    private void loadData() {
        if (mbase == null) {
            Log.e("MainActivity", "DatabaseReference is null. Firebase setup issue.");
            return;
        }
        recyclerView.setLayoutManager(gridLayoutManager);
        list=new ArrayList<>();
        campainAdapter=new CountryAdapter(this,list);
        recyclerView.setAdapter(campainAdapter );
        Query query = mbase.limitToFirst(PAGE_SIZE);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        campainModel = dataSnapshot.getValue(CountryModel.class);
                        String key = dataSnapshot.getKey();
                        if (campainModel != null) {
                            list.add(campainModel);
                        }
                    }
                    campainAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
    private void searchList(String newText) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<CountryModel> fList = new ArrayList<>();
                for (CountryModel obj : list) {
                    if (obj.getName().toLowerCase().contains(newText.toLowerCase())) {
                        fList.add(obj);
                    }
                }

                // Update the UI on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        campainAdapter.searchLists((ArrayList<CountryModel>) fList);
                        campainAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }


    public void navItemOnClickListener(){


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {



                if(item.getItemId()==R.id.menu_guide_id){

                    Intent openWebsiteIntent = new Intent(MainActivity.this, PostHandleActivity.class);

                    startActivity(openWebsiteIntent);

                }

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
                        intent.putExtra(Intent.EXTRA_SUBJECT,"✔✔ Best Application for Students Carrier and University Guidelines");
                        intent.putExtra(Intent.EXTRA_TEXT,"\uD83D\uDC49 "+"https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());

                        startActivity(Intent.createChooser(intent,"Share With"));
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Unable to Share!!!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                if(item.getItemId()==R.id.menu_popular_content_id){
                    Intent intent=new Intent(MainActivity.this, CategoryPostActivity.class);
                    intent.putExtra("title","Most Popular Content");
                    startActivity(intent);
                }
                if(item.getItemId()==R.id.menu_facebook_id){
                    String websiteUrl = "https://www.facebook.com/profile.php?id=61558689002953&mibextid=ZbWKwL";
                    Intent openWebsiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                    startActivity(openWebsiteIntent);

                }
                if(item.getItemId()==R.id.menu_telegram_id){
                    String websiteUrl = "https://t.me/globaluniguide";
                    Intent openWebsiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                    startActivity(openWebsiteIntent);

                }
                if(item.getItemId()==R.id.menu_favorite_uni_id){
                    Intent intent=new Intent(MainActivity.this, ReBookMarkActivity.class);
                    intent.putExtra("check",2);
                    startActivity(intent);

                }
                if(item.getItemId()==R.id.menu_recent_id){
                    Intent intent=new Intent(MainActivity.this, ReBookMarkActivity.class);
                    intent.putExtra("check",1);
                    startActivity(intent);

                }
                if(item.getItemId()==R.id.menu_vocabulayr_id){
                    Intent intent=new Intent(MainActivity.this, VocabularyActivity.class);
                    startActivity(intent);

                }



                return false;
            }
        });

    }
    @Override
    public void onBackPressed() {
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            int firstVisibleItemPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            if (firstVisibleItemPosition > 0) {
                // Scroll to the first item
                recyclerView.smoothScrollToPosition(0);
            } else {
                // Display the exit confirmation dialog
                showExitConfirmationDialog();
            }
        } else {
            // If recyclerView or its adapter is null, display the exit confirmation dialog
            showExitConfirmationDialog();
        }
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder alertObj = new AlertDialog.Builder(MainActivity.this);
        alertObj.setTitle(Html.fromHtml("<font color='#000000'>Confirm Exit...ℹ️</font>"));
        alertObj.setMessage(Html.fromHtml("<font color='#000000'>ℹ️ Do you want to Exit this Application ❓❓</font>"));

        alertObj.setPositiveButton(Html.fromHtml("<font color='#000000'>✅Yes</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finishAffinity();
            }
        });

        alertObj.setNegativeButton(Html.fromHtml("<font color='#000000'>❌No</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = alertObj.create();
        dialog.show();

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.alert_back);
        dialog.getWindow().setBackgroundDrawable(drawable);
    }



}