package com.techtravelcoder.alluniversityinformations.web;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.ads.App;
import com.techtravelcoder.alluniversityinformations.countryDetails.MainActivity;
import com.techtravelcoder.alluniversityinformations.universityDetails.UniversityActivity;
import com.techtravelcoder.alluniversityinformations.universityDetails.UniversityAdapterWeb;
import com.techtravelcoder.alluniversityinformations.universityDetails.UniversityModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import co.notix.interstitial.NotixInterstitial;
import kotlin.Unit;

public class UniversityWebActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    UniversityModel universityModel;

    private StartAppAd startAppAd = new StartAppAd(this);

    private Toolbar toolbar;
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
        String uni_name=getIntent().getStringExtra("name");
        String country_name=getIntent().getStringExtra("country");


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
        if(item.getItemId() == R.id.menu_home_id) {
            Intent intent = new Intent(UniversityWebActivity.this, MainActivity.class);
            startActivity(intent);
            return true; // Make sure to return true to indicate that the item click is handled
        }
        if(item.getItemId() == R.id.menu_fovorite_id) {
            Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show();
            return true; // Make sure to return true to indicate that the item click is handled
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
            App.interstitialLoader.doOnNextAvailable(result -> {
                if (result != null) {
                    NotixInterstitial.Companion.show(result);
                }
                return Unit.INSTANCE;
            });

            super.onBackPressed();
        }
    }
}
