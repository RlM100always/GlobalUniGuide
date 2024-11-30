package com.techtravelcoder.alluniversityinformations.pdf;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.ads.GoogleSignInHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class PDFShowActivity extends AppCompatActivity {

    private PDFView pdfView;
    private DatabaseHelper databaseHelper;
    private Toolbar title;
    private static final int RC_SIGN_IN = 9001;

    private  String fileName;
    private static final String PREFS_NAME = "PDFPrefs";
    private static final String KEY_PAGE_NUMBER = "pageNumber";
    private int currentPage = 0;
    TextView cancel;

    private ImageView settings,withoutBookMark,home,share;
    private String theme,mode,fileUrl,fName,bName,iUrl;
    private GoogleSignInHelper mGoogleSignInHelper;
    private LinearLayout pdfsettings,noInternet;
    private WebView webView;
    private int key;
    private LinearLayout s_ll;
    private String accessToken;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfshow);

        pdfView=findViewById(R.id.pdfView1);
        webView=findViewById(R.id.pdf_webview_id);
        title=findViewById(R.id.custom_toolbar);
        setSupportActionBar(title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        settings=findViewById(R.id.settings_button_id);
        withoutBookMark=findViewById(R.id.without_bookmark_button_id);
        share=findViewById(R.id.share_button_id);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        pdfsettings=findViewById(R.id.all_pdf_settings_ll);
        noInternet=findViewById(R.id.no_internet_connect_ll);
        s_ll=findViewById(R.id.settings_ll);

        title.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        databaseHelper = new DatabaseHelper(PDFShowActivity.this);

        String fUrl=getIntent().getStringExtra("fUrl");
        fName=getIntent().getStringExtra("fName");
        iUrl=getIntent().getStringExtra("iUrl");
        bName=getIntent().getStringExtra("bookName");
        key=getIntent().getIntExtra("check",0);
        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.back);
        }
        getWindow().setStatusBarColor(color);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        if(FirebaseAuth.getInstance().getUid()!=null){

            fileUrl = "https://drive.google.com/uc?export=download&id=" + fUrl;
            fileName = fName + fUrl;

            if(key==2)
            {
                toolbarTitle.setText("" + bName);//toolbar
                s_ll.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.setWebViewClient(new WebViewClient()); // Prevent opening the PDF in a browser
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true); // Enable JavaScript
                webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // Enable caching
                webSettings.setLoadWithOverviewMode(true); // Optimize content to fit WebView
                webSettings.setUseWideViewPort(true); // Allow web pages to be scalable
                webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                webView.setDownloadListener(new DownloadListener() {
                    @Override
                    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                        if(isStoragePermissionGranted()){
                            Toast.makeText(PDFShowActivity.this, "Download Started!!!", Toast.LENGTH_SHORT).show();
                            // databaseHelper.deleteFile(fileName);
                            FileDownloader.downloadFile(PDFShowActivity.this, url, fileName, new FileDownloader.DownloadListener() {
                                @Override
                                public void onDownloadComplete(File file) {
                                    databaseHelper.addFile(fileName, fileUrl, file.getAbsolutePath());
                                    Toast.makeText(PDFShowActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();


                                }


                                @Override
                                public void onDownloadFailed(Exception e) {
                                    Toast.makeText(getApplicationContext(), "Internet Problem", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            isStoragePermissionGranted();
                            Toast.makeText(PDFShowActivity.this, "Storage permission is required", Toast.LENGTH_SHORT).show();

                        }


                    }
                });
                String googleDriveViewerUrl="https://drive.google.com/file/d/"+fUrl+"/view?usp=sharing";
                webView.getSettings().setJavaScriptEnabled(true);
                if (isInternetAvailable()) {
                    webView.loadUrl(googleDriveViewerUrl);
                } else {
                    // Show a toast if there is no internet connection
                    Toast.makeText(PDFShowActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
            else {
                pdfView.setVisibility(View.VISIBLE);
                s_ll.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                toolbarTitle.setText("" + bName);//toolbar


                if (databaseHelper.fileExists(fileUrl))
                {
                    FirebaseDatabase.getInstance().getReference("UserInfo").child(FirebaseAuth.getInstance().getUid()).child("userPdfSettings")
                            .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                mode = (String) snapshot.child("viewMode").getValue();
                                theme = (String) snapshot.child("themeMode").getValue();
                                loadPdf(databaseHelper.getFilePath(fileUrl), mode, theme);

                            } else {
                                mode = "horizontal";
                                theme = "light";
                                loadPdf(databaseHelper.getFilePath(fileUrl), mode, theme);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {

                    Toast.makeText(this, "Please Download Books From Online First!!!!", Toast.LENGTH_SHORT).show();

                }

            }


            FirebaseDatabase.getInstance().getReference("Book Details").child(fName).child("bookmarkbook")
                    .child(FirebaseAuth.getInstance().getUid()).child("userBookmark").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Boolean bool = (Boolean) snapshot.getValue();
                                if (bool) {
                                    withoutBookMark.setBackgroundResource(R.drawable.b1);

                                } else {
                                    withoutBookMark.setBackgroundResource(R.drawable.b2);

                                }


                            } else {
                                withoutBookMark.setBackgroundResource(R.drawable.b2);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    settingsHandle();
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String imageUrl = iUrl;
                    try {
                        if (imageUrl != null) {
                            // Create the text part
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*"); // MIME type for image sharing
                            String subject = "✔✔ Book Name: " + bName + "\n\n" +
                                    "✔✔GlobalUniGuide  Provides A Huge Collection of Books,dictionary,educational contents for University Students , College Students , and Learners ." + "\n\n1. This App Provides daily Books, Also you can request to Upload your desire books to Admin" + "\n\n2.We Have A millions Of Books Collection on Science" +
                                    "Arts , Commerce ,Programming , Technology , Economics , Business , Motivation ,Self Study ,Engineering ,Medical all types of educational contents...and Its Totally Free" ;
                            String appLink = "\nDownload And Read Your Desire Books: \n\n" + "\uD83D\uDC49 \uD83D\uDC49" + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
                            String message = subject + "\n\n" + appLink;
                            intent.putExtra(Intent.EXTRA_TEXT, message);

                            // Load image from URL using Glide
                            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load(imageUrl)
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                            try {
                                                // Save bitmap to cache directory
                                                File cachePath = new File(getCacheDir(), "images");
                                                cachePath.mkdirs(); // Create directory if not exists
                                                File imageFile = new File(cachePath, "shared_image.png");
                                                FileOutputStream stream = new FileOutputStream(imageFile);
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                stream.close();

                                                // Get content URI using FileProvider
                                                Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.techtravelcoder.educationalbooks.fileprovider", imageFile);
                                                intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant temporary read permission

                                                // Show the chooser dialog
                                                startActivity(Intent.createChooser(intent, "Share With"));
                                            } catch (IOException e) {
                                                Toast.makeText(PDFShowActivity.this, "Failed to save image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onLoadCleared(Drawable placeholder) {
                                            // Optional: Handle placeholder if needed
                                        }

                                        @Override
                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                            Toast.makeText(PDFShowActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(PDFShowActivity.this, "Internet Connection Loss", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(PDFShowActivity.this, "Unable to Share!!! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            withoutBookMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookBookMarkHandle();
                }
            });


        }
        else {
            Toast.makeText(this, "Not Login", Toast.LENGTH_SHORT).show();
        }


    }





    private void bookBookMarkHandle() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            try {
                FirebaseDatabase.getInstance().getReference("Book Details").child(fName).child("bookmarkbook").child(FirebaseAuth.getInstance().getUid())
                        .child("userBookmark")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Boolean bool= (Boolean) snapshot.getValue();

                                    if(bool){
                                        databaseReference= FirebaseDatabase.getInstance().getReference("Book Details").child(fName).child("bookmarkbook").child(FirebaseAuth.getInstance().getUid());

                                        databaseReference.child("userBookmark").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        withoutBookMark.setBackgroundResource(R.drawable.b2);

                                                        Toast.makeText(PDFShowActivity.this, "Successfully Remove From The BookMark List", Toast.LENGTH_SHORT).show();

                                                    }
                                                });

                                    }
                                    else {
                                        databaseReference= FirebaseDatabase.getInstance().getReference("Book Details").child(fName).child("bookmarkbook").child(FirebaseAuth.getInstance().getUid());

                                        databaseReference.child("userBookmark").setValue(true);
                                        databaseReference.child("time").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        withoutBookMark.setBackgroundResource(R.drawable.b1);
                                                        Toast.makeText(PDFShowActivity.this, "Successfully Added To The BookMark List", Toast.LENGTH_SHORT).show();

                                                    }
                                                });

                                    }

                                }
                                else {
                                    databaseReference= FirebaseDatabase.getInstance().getReference("Book Details").child(fName).child("bookmarkbook").child(FirebaseAuth.getInstance().getUid());
                                    databaseReference.child("userBookmark").setValue(true);
                                    databaseReference.child("time").setValue(ServerValue.TIMESTAMP).
                                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            withoutBookMark.setBackgroundResource(R.drawable.b2);
                                            Toast.makeText(PDFShowActivity.this, "Successfully Added To The Bookmark List", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(PDFShowActivity.this, "Network Problem!!!", Toast.LENGTH_SHORT).show();

                                                }
                                            });


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }catch (Exception e){
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
        }

    }

    private void pdfSettingsChecker() {
        FirebaseDatabase.getInstance().getReference("UserInfo").child(FirebaseAuth.getInstance().getUid()).child("userPdfSettings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    mode= (String) snapshot.child("viewMode").getValue();
                    theme= (String) snapshot.child("themeMode").getValue();
                    if (isActivityRunning()) {
                        dialogue();
                    }


                }
                else {
                    mode="horizontal";
                    theme="light";
                    if (isActivityRunning()) {
                        dialogue();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void dialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PDFShowActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.pdf_settings, null);
        RadioGroup viewhv = view.findViewById(R.id.group_radio_view);
        RadioGroup modess = view.findViewById(R.id.radio_group_mode);

        RadioButton horizontal = view.findViewById(R.id.horizontal_id);
        RadioButton vertical = view.findViewById(R.id.vertical_id);
        RadioButton light = view.findViewById(R.id.light_id);
        RadioButton night = view.findViewById(R.id.night_id);

        if(Build.VERSION.SDK_INT >= 21)
        {
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]
                            {
                                    new int[]{-android.R.attr.state_enabled}, // Disabled
                                    new int[]{android.R.attr.state_enabled}   // Enabled
                            },
                    new int[]
                            {
                                    Color.MAGENTA, // disabled
                                    Color.MAGENTA   // enabled
                            }
            );

            horizontal.setButtonTintList(colorStateList);
            horizontal.invalidate();
            vertical.setButtonTintList(colorStateList);
            vertical.invalidate();
            light.setButtonTintList(colorStateList);
            light.invalidate();
            night.setButtonTintList(colorStateList);
            night.invalidate();
        }

        if(theme.equals("light")){
            light.setChecked(true);
        }else {
            night.setChecked(true);
        }

        if(mode.equals("horizontal")){
            horizontal.setChecked(true);
        }else {
            vertical.setChecked(true);
        }

        TextView applySettings = view.findViewById(R.id.apply_settings_id);
        cancel = view.findViewById(R.id.cancel_settings_id);


        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.alert_back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();


        applySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertDialog!=null && alertDialog.isShowing()){
                    alertDialog.dismiss();
                    finish();
                }
                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    int selectedViewModeId = viewhv.getCheckedRadioButtonId();
                    int selectedThemeModeId = modess.getCheckedRadioButtonId();

                    String viewMode = "";
                    String themeMode = "";

                    if (selectedViewModeId == horizontal.getId()) {
                        viewMode = "horizontal";
                    }
                    else if (selectedViewModeId == vertical.getId()) {
                        viewMode = "vertical";
                    }

                    if (selectedThemeModeId == light.getId()) {
                        themeMode = "light";
                    }
                    else if (selectedThemeModeId == night.getId()) {
                        themeMode = "night";
                    }


                    if (!viewMode.isEmpty() && !themeMode.isEmpty()) {
                        // Create a HashMap to store the settings
                        HashMap<String, Object> settingsMap = new HashMap<>();
                        settingsMap.put("viewMode", viewMode);
                        settingsMap.put("themeMode", themeMode);


                        // Reference to the Firebase Realtime Database
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo").child(FirebaseAuth.getInstance().getUid());

                        // Update the settings in Firebase
                        databaseReference.child("userPdfSettings").setValue(settingsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PDFShowActivity.this, "Settings updated successfully", Toast.LENGTH_SHORT).show();


                                    FirebaseDatabase.getInstance().getReference("UserInfo").child(FirebaseAuth.getInstance().getUid()).child("userPdfSettings").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                mode= (String) snapshot.child("viewMode").getValue();
                                                theme= (String) snapshot.child("themeMode").getValue();
                                                loadPdf(databaseHelper.getFilePath(fileUrl),mode,theme);


                                            }
                                            else {
                                                mode="light";
                                                theme="horizontal";
                                                loadPdf(databaseHelper.getFilePath(fileUrl),mode,theme);

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            alertDialog.dismiss();

                                        }
                                    });


                                }
                                else {
                                    Toast.makeText(PDFShowActivity.this, "Failed to update settings", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();

                                }
                            }
                        });

                    }
                    else {
                        Toast.makeText(PDFShowActivity.this, "Please select both view mode and theme mode", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    }
                }
                else {
                    Toast.makeText(PDFShowActivity.this, "Login First", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void settingsHandle() {
        pdfSettingsChecker();

    }

    private boolean isStoragePermissionGranted() {
        if (ContextCompat.checkSelfPermission(PDFShowActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true; // Permission is granted
        } else {
            // Permission is not granted, redirect to settings
            goToAppSettings();
            return false;
        }
    }

    private void goToAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }







    private void savePageNumber(String filename, int pageNumber) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(filename, pageNumber);
        editor.apply();
    }

    private int getSavedPageNumber(String filename) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedPage = prefs.getInt(filename, 0);
        return savedPage;
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePageNumber(fileName, currentPage);
    }

    private void loadPdf(String filePath,String mode ,String theme) {


        if (filePath != null) {
            File file = new File(filePath);

            if (file.exists()) {

                if(theme.equals("light") && mode.equals("horizontal")){
                    pdfView.fromFile(file)
                            .defaultPage(getSavedPageNumber(fileName))
                            .swipeHorizontal(true) // Enable horizontal swiping
                            .enableDoubletap(true)
                            .onLoad(nbPages -> {})
                            .onPageChange((page, pageCount) -> {})
                            .onError(t -> {})
                            .onPageError((page, t) -> {})
                            .enableAnnotationRendering(false)
                            .password(null)
                            .scrollHandle(null)
                            .onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    currentPage = page;
                                    savePageNumber(fileName,page);
                                }
                            })

                            .enableAntialiasing(true)
                            .spacing(0)
                            .scrollHandle(new DefaultScrollHandle(this))
                            .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                            .pageSnap(true) // snap pages to screen boundaries
                            .pageFling(true) // make a fling change only a single page like ViewPager
                            .nightMode(false)
                            .fitEachPage(true)
                            .load();

                }
                if(theme.equals("light") && mode.equals("vertical")){
                    pdfView.fromFile(file)
                            .defaultPage(getSavedPageNumber(fileName))
                            .swipeHorizontal(false) // Enable horizontal swiping
                            .enableDoubletap(true)
                            .onLoad(nbPages -> {})
                            .onPageChange((page, pageCount) -> {})
                            .onError(t -> {})
                            .onPageError((page, t) -> {})
                            .enableAnnotationRendering(false)
                            .password(null)
                            .scrollHandle(null)
                            .enableAntialiasing(true)
                            .spacing(0)
                            .onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    currentPage = page;
                                    savePageNumber(fileName,page);
                                }
                            })
                            .scrollHandle(new DefaultScrollHandle(this))
                            .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                            .nightMode(false)
                            .fitEachPage(true)
                            .load();

                }
                if(theme.equals("night") && mode.equals("vertical")){
                    pdfView.fromFile(file)
                            .defaultPage(getSavedPageNumber(fileName))

                            .swipeHorizontal(false) // Enable horizontal swiping
                            .enableDoubletap(true)
                            .onLoad(nbPages -> {})
                            .onPageChange((page, pageCount) -> {})
                            .onError(t -> {})
                            .onPageError((page, t) -> {})
                            .enableAnnotationRendering(false)
                            .password(null)
                            .onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    currentPage = page;
                                    savePageNumber(fileName,page);
                                }
                            })
                            .scrollHandle(null)
                            .enableAntialiasing(true)
                            .spacing(0)
                            .scrollHandle(new DefaultScrollHandle(this))
                            .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                            .nightMode(true)
                            .load();

                }
                if(theme.equals("night") && mode.equals("horizontal")){
                    pdfView.fromFile(file)
                            .defaultPage(getSavedPageNumber(fileName))

                            .swipeHorizontal(true) // Enable horizontal swiping
                            .enableDoubletap(true)
                            .onLoad(nbPages -> {})
                            .onPageChange((page, pageCount) -> {})
                            .onError(t -> {})
                            .onPageError((page, t) -> {})
                            .enableAnnotationRendering(false)
                            .password(null)
                            .onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    currentPage = page;
                                    savePageNumber(fileName,page);
                                }
                            })
                            .scrollHandle(null)
                            .enableAntialiasing(true)
                            .spacing(0)
                            .scrollHandle(new DefaultScrollHandle(this))
                            .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                            .pageSnap(true) // snap pages to screen boundaries
                            .pageFling(true) // make a fling change only a single page like ViewPager
                            .nightMode(true)
                            .load();

                }


            }
            else {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "File path not found in database", Toast.LENGTH_SHORT).show();
        }
    }



    public boolean isActivityRunning() {
        return !isFinishing() && !isDestroyed();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();  // WebView navigates back
        } else {
            super.onBackPressed();  // Default back action (e.g., closing the activity)
        }
    }

}
