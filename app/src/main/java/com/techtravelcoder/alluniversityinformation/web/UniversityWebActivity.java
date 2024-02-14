package com.techtravelcoder.alluniversityinformation.web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformation.universityDetails.UniversityAdapterWeb;
import com.techtravelcoder.alluniversityinformation.universityDetails.UniversityModel;

import java.util.ArrayList;
import java.util.Collections;

public class UniversityWebActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    UniversityModel universityModel;

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

        webView = findViewById(R.id.webview_id);
        progressBar = findViewById(R.id.progressBar);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(s_link);

        suggestedUniLoad();
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

        mbase.addValueEventListener(new ValueEventListener() {
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
            super.onBackPressed();
        }
    }
}
