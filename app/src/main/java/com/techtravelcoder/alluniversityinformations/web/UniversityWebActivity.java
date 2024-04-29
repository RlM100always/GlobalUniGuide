package com.techtravelcoder.alluniversityinformations.web;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.startapp.sdk.adsbase.StartAppAd;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.countryDetails.MainActivity;
import com.techtravelcoder.alluniversityinformations.postDetails.RatingModel;
import com.techtravelcoder.alluniversityinformations.universityDetails.SeeUniPostActivity;
import com.techtravelcoder.alluniversityinformations.universityDetails.UniversityActivity;
import com.techtravelcoder.alluniversityinformations.universityDetails.UniversityAdapterWeb;
import com.techtravelcoder.alluniversityinformations.universityDetails.UniversityModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class UniversityWebActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    UniversityModel universityModel;

    private StartAppAd startAppAd = new StartAppAd(this);

    private Toolbar toolbar;
    private String keyUni;
    private Long bookmark,rateno;
    private Double avgRate;
    private String uni_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_web);

        int color = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.back);
        }
        getWindow().setStatusBarColor(color);

        String s_link = getIntent().getStringExtra("link");
         uni_name=getIntent().getStringExtra("name");
        String country_name=getIntent().getStringExtra("country");

        keyUni=getIntent().getStringExtra("key");
        bookmark=getIntent().getLongExtra("bookmark",0);
        rateno=getIntent().getLongExtra("reviewers",0);
        avgRate=getIntent().getDoubleExtra("avgrate",0d);



        //toolbar code
        toolbar=findViewById(R.id.tolbar_web);
        setSupportActionBar(toolbar);
        toolbar.setTitle(uni_name);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),UniversityActivity.class);
                intent.putExtra("name",country_name);
                startActivity(intent);
            }
        });



        //handelling webview
        webView = findViewById(R.id.webview_id);
        progressBar = findViewById(R.id.progressBar);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);

        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(s_link);

        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            // Implement download handling here
            // For example, you can use DownloadManager to handle the download
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setMimeType(mimetype);
            String cookies = CookieManager.getInstance().getCookie(url);
            request.addRequestHeader("Cookie", cookies);
            request.addRequestHeader("User-Agent", userAgent);
            request.setDescription("Downloading file...");
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));

            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                downloadManager.enqueue(request);
            } else {
                Toast.makeText(getApplicationContext(), "Download Manager is not available", Toast.LENGTH_SHORT).show();
            }
        });


        suggestedUniLoad();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("favorite")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Boolean bool= (Boolean) snapshot.getValue();
                            //Toast.makeText(PostWebViewActivity.this, "Menu", Toast.LENGTH_SHORT).show();
                            Menu menu = toolbar.getMenu();; // Replace with your menu ID
                            MenuItem menuItem = menu.findItem(R.id.menu_favorite_border_id); // ID of the menu item to update

                            if (bool==true) {
                                // Update the menu item properties
                                menuItem.setTitle("Remove from BookMark");
                                menuItem.setIcon(R.drawable.baseline_bookmark_added_24);
                            } else if (bool==false) {
                                menuItem.setTitle("Add to BookMark");
                                menuItem.setIcon(R.drawable.baseline_bookmark_border_24);
                            }else {
                                menuItem.setTitle("Add to BookMark");
                                menuItem.setIcon(R.drawable.baseline_bookmark_border_24);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        if(item.getItemId() == R.id.menu_home_id) {
            Intent intent = new Intent(UniversityWebActivity.this, MainActivity.class);
            intent.putExtra("val",2);

            startActivity(intent);
            return true; // Make sure to return true to indicate that the item click is handled
        }
        if(item.getItemId() == R.id.menu_favorite_border_id) {
            FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("favorite")
                    .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Boolean bool= (Boolean) snapshot.getValue();

                                if(bool)
                                {
                                    FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("favorite")
                                            .child(FirebaseAuth.getInstance().getUid()).setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(UniversityWebActivity.this, "Successfully remove from the Bookmark List", Toast.LENGTH_SHORT).show();
                                                    FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("postLoves").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            Long num=snapshot.getValue(Long.class);
                                                            if(snapshot.exists()){
                                                                FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("postLoves").setValue(num-1L);

                                                            }else {
                                                                FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("postLoves").setValue(1L);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                }
                                            });

                                }else {
                                    FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("favorite")
                                            .child(FirebaseAuth.getInstance().getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(UniversityWebActivity.this, "Successfully added to the BookMark List", Toast.LENGTH_SHORT).show();
                                                    FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("postLoves").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            Long num=snapshot.getValue(Long.class);
                                                            if(snapshot.exists()){
                                                                FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("postLoves").setValue(num+1L);

                                                            }else {
                                                                FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("postLoves").setValue(1L);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                }
                                            });
                                }

                            }else {
                                FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("favorite")
                                        .child(FirebaseAuth.getInstance().getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(UniversityWebActivity.this, "Successfully added to the BookMark List", Toast.LENGTH_SHORT).show();
                                                FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("postLoves").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        Long num=snapshot.getValue(Long.class);
                                                        if(snapshot.exists()){
                                                            FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("postLoves").setValue(num+1L);

                                                        }else {
                                                            FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("postLoves").setValue(1L);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


        }
        if(item.getItemId()==R.id.menu_rating_id){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.rating_design, null);
            builder.setView(dialogView);

            RatingBar ratingBar=dialogView.findViewById(R.id.ratingBar);
            EditText ratingText=dialogView.findViewById(R.id.rating_reason_id);
            EditText ratingName=dialogView.findViewById(R.id.rating_name_id);
            TextView submit=dialogView.findViewById(R.id.rating_submit_id);

            //retrive
            FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("rating")
                    .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                ratingName.setText(snapshot.child("name").getValue(String.class));
                                ratingText.setText(snapshot.child("text").getValue(String.class));
                                Double db=snapshot.child("rate").getValue(Double.class);
                                DecimalFormat df = new DecimalFormat("#.##"); // Two decimal places
                                float myFloat = Float.parseFloat(df.format(db));
                                ratingBar.setRating(myFloat);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



            // Create and show the dialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.alert_back);
            alertDialog.getWindow().setBackgroundDrawable(drawable);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rating=String.valueOf(ratingBar.getRating());

                    if(!rating.isEmpty() && !ratingName.getText().toString().isEmpty() && !ratingText.getText().toString().isEmpty()){

                        Calendar calendar = Calendar.getInstance();
                        Date times=calendar.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
                        String date = sdf.format(times);

                        Map<String,Object> map=new HashMap();
                        map.put("userid",FirebaseAuth.getInstance().getUid());
                        map.put("name",ratingName.getText().toString());
                        map.put("rate",Double.parseDouble(rating));
                        map.put("text",ratingText.getText().toString());
                        map.put("date",date);

                        //retrive
                        FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("ratingNum").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("rating")
                                            .child(FirebaseAuth.getInstance().getUid()).setValue(map);
                                    Toast.makeText(UniversityWebActivity.this, "Successful", Toast.LENGTH_SHORT).show();

                                    FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Double rate=0d;
                                            Long count=0L;
                                            if(snapshot.exists()){
                                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                    RatingModel ratingModel=dataSnapshot.getValue(RatingModel.class);

                                                    rate=rate+ratingModel.getRate();
                                                    count++;

                                                }
                                            }

                                            FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("ratingNum").setValue(count);
                                            Double v1 = rate / count;
                                            FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("avgRating").setValue(v1);
                                            alertDialog.dismiss();



                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                                else {
                                    //add
                                    FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("rating")
                                            .child(FirebaseAuth.getInstance().getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(UniversityWebActivity.this, "Rating Successful", Toast.LENGTH_SHORT).show();

                                                    FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("ratingNum").setValue(1L);
                                                    Double d=Double.parseDouble(rating);
                                                    FirebaseDatabase.getInstance().getReference("University").child(keyUni).child("avgRating").setValue(d);
                                                    alertDialog.dismiss();


                                                }
                                            });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                        //add


                    }

                }
            });
        }
        if(item.getItemId()==R.id.menu_university_details_id){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.uni_summary, null);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
            TextView reviewNo,bmark,rate;
            TextView userWhoRate ;
            TextView uniName,cancel;
            reviewNo=dialogView.findViewById(R.id.uni_samary_reviews_no);
            bmark=dialogView.findViewById(R.id.uni_samary_bookmark);
            rate=dialogView.findViewById(R.id.uni_samary_avg_star);
            uniName=dialogView.findViewById(R.id.uni_samary_universityName);
            cancel=dialogView.findViewById(R.id.uni_samary_cancel_id);
            userWhoRate=dialogView.findViewById(R.id.see_uni_samary_userwhorate);

            uniName.setText(uni_name+" Summary Based on user Activities");

            if(bookmark==0){
                bmark.setText("No BookMark History");
            }else {
                bmark.setText(bookmark+" People BookMark");
            }

            if(rateno==0){
                reviewNo.setText("No Review History");
            }else {
                reviewNo.setText(rateno+" People Reviews");
            }

            if(avgRate==0.0){
                rate.setText("No Rating History");
            }else {
                rate.setText(avgRate+" Stars");
            }
            userWhoRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), SeeUniPostActivity.class);
                    intent.putExtra("postid",keyUni);
                    intent.putExtra("checker","2");
                    startActivity(intent);
                }
            });
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.alert_back);
            alertDialog.getWindow().setBackgroundDrawable(drawable);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    private void suggestedUniLoad() {
        RecyclerView recyclerViewSug;
        UniversityAdapterWeb universityAdapterWeb;
        ArrayList<UniversityModel> list;

        DatabaseReference mbase;
        recyclerViewSug = findViewById(R.id.suggested_recycler_id);
        mbase = FirebaseDatabase.getInstance().getReference("University");

        recyclerViewSug.setLayoutManager(new GridLayoutManager(this,1,GridLayoutManager.HORIZONTAL,false));
        list = new ArrayList<>();
        universityAdapterWeb = new UniversityAdapterWeb(this, list);
        recyclerViewSug.setAdapter(universityAdapterWeb);

        Random random=new Random();
        int num=random.nextInt(100)+100;

        Random random1=new Random();
        int num1=random1.nextInt(3);

        if(num1==0)
        {
            Query query=mbase.limitToFirst(num);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();

                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            universityModel = dataSnapshot.getValue(UniversityModel.class);
                            //&& campainModel.getCountryName().equals(contryName)

                            if (universityModel != null && universityModel.getContryName() != null
                                    && universityModel.getSuggested().equals("true")) {
                                // Ensure this operation is performed on the main (UI) thread
                                runOnUiThread(() -> {
                                    list.add(0, universityModel);
                                    universityAdapterWeb.notifyDataSetChanged();
                                    Collections.shuffle(list);
                                    universityAdapterWeb.notifyDataSetChanged();
                                });
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });

        }
        else if(num1==1)
        {
            Query query=mbase.limitToLast(num);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();

                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            universityModel = dataSnapshot.getValue(UniversityModel.class);
                            //&& campainModel.getCountryName().equals(contryName)

                            if (universityModel != null && universityModel.getContryName() != null
                                    && universityModel.getSuggested().equals("true")) {
                                // Ensure this operation is performed on the main (UI) thread
                                runOnUiThread(() -> {
                                    list.add(0, universityModel);
                                    universityAdapterWeb.notifyDataSetChanged();
                                    Collections.shuffle(list);
                                    universityAdapterWeb.notifyDataSetChanged();
                                });
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });
        }
        else if(num1==2){
            Random random2=new Random();
            int randomNumber2 = random2.nextInt(20) + 200;
            Query query = mbase.orderByKey().startAt(String.valueOf(randomNumber2));
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();

                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            universityModel = dataSnapshot.getValue(UniversityModel.class);
                            //&& campainModel.getCountryName().equals(contryName)

                            if (universityModel != null && universityModel.getContryName() != null
                                    && universityModel.getSuggested().equals("true")) {
                                // Ensure this operation is performed on the main (UI) thread
                                runOnUiThread(() -> {
                                    list.add(0, universityModel);
                                    universityAdapterWeb.notifyDataSetChanged();
                                    Collections.shuffle(list);
                                    universityAdapterWeb.notifyDataSetChanged();
                                });
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // Show the ProgressBar when the page starts loading
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // Hide the ProgressBar when the page finishes loading
            progressBar.setVisibility(View.GONE);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // Update the progress bar during page loading
            progressBar.setProgress(newProgress);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
//            App.interstitialLoader.doOnNextAvailable(result -> {
//                if (result != null) {
//                    NotixInterstitial.Companion.show(result);
//                }
//                return Unit.INSTANCE;
//            });

            super.onBackPressed();
        }
    }
}
