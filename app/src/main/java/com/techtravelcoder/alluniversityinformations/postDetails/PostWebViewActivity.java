package com.techtravelcoder.alluniversityinformations.postDetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.FragmentAdapter.PostWebAdapter;
import com.techtravelcoder.alluniversityinformations.FragmentModel.MainPostModel;
import com.techtravelcoder.alluniversityinformations.ads.GoogleSignInHelper;
import com.techtravelcoder.alluniversityinformations.countryDetails.MainActivity;
import com.techtravelcoder.alluniversityinformations.universityDetails.SeeUniPostActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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

public class PostWebViewActivity extends AppCompatActivity {

    private WebView webView;
    private RecyclerView recyclerView;
    private PostWebAdapter postWebAdapter;
    private ArrayList<MainPostModel> list;
    private DatabaseReference databaseReference;
    private TextView titles,mayLike,loading;
    private ProgressBar progressBar;
    private  String label,key;
    private Long views;
    private Handler handler;
    private Toolbar toolbars;
    private String title,selfLink,urlLink;
    private String htmlContentWithStyle;
    private TextToSpeech textToSpeech;
    private Boolean bool=false;
    private Long loves,rateno;
    private Double avgRate;
    private LinearLayout postInfo;
    private TextView lovesPost,reviewNoPost,avgReviewPost,reviewsersTv;
    private String text;

    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private AlertDialog alertDialog;
    private String userName,userImage;

    private GoogleSignInHelper mGoogleSignInHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_web_view);



        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.whiteTextSideColor1);
        }
        getWindow().setStatusBarColor(color);


        recyclerView=findViewById(R.id.post_recyclerView);
        webView=findViewById(R.id.post_webView);
        titles=findViewById(R.id.post_title_id);
        mayLike=findViewById(R.id.may_like_id);
        loading=findViewById(R.id.post_loading_id);
        postInfo=findViewById(R.id.ll_info_id);
        lovesPost=findViewById(R.id.post_webview_loves_id);
        reviewNoPost=findViewById(R.id.post_webview_review_num);
        avgReviewPost=findViewById(R.id.post_webview_avgstar);
        reviewsersTv=findViewById(R.id.post_webview_reviewers_id);


        progressBar=findViewById(R.id.post_progressbar_id);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.whiteTextColor1), PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);


         toolbars=findViewById(R.id.post_web_tolbar);
         setSupportActionBar(toolbars);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(PostWebViewActivity.this, "Language not supported", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PostWebViewActivity.this, "Initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                // Speech started
            }

            @Override
            public void onDone(String utteranceId) {
                // Speech completed
            }

            @Override
            public void onError(String utteranceId) {
                // Speech error
            }
        });

         toolbars.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), PostHandleActivity.class);
                if(textToSpeech!=null){
                    textToSpeech.stop();
                }
                startActivity(intent);
            }
        });

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);
        webView.setHapticFeedbackEnabled(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript
        webSettings.setDomStorageEnabled(true); // Enable DOM Storage
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);




        retrivePostData();
        retriveUserData();



        //String title = getIntent().getStringExtra("title");
        String postid=getIntent().getStringExtra("postId");
        label=getIntent().getStringExtra("label");
        views=getIntent().getLongExtra("num",1);
        key=getIntent().getStringExtra("key");
        loves=getIntent().getLongExtra("loves",0);
        rateno=getIntent().getLongExtra("reviewers",0);
        avgRate=getIntent().getDoubleExtra("avgrate",0d);
        toolbars.setTitle(label);



        reviewsersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SeeUniPostActivity.class);
                intent.putExtra("postid",key);
                intent.putExtra("checker","1");

                startActivity(intent);
            }
        });



        //favorite menu handle dynamically



        String url="https://www.googleapis.com/blogger/v3/blogs/4823373246423002249/posts/"+postid+"?key=AIzaSyDRWqHviRcT8mTswe19KPBDIVlxfdIcejM";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Parse the response JSON to get the single post data
                    JSONObject jsonObject = new JSONObject(response);

                    title = jsonObject.getString("title");
                    String content = jsonObject.getString("content");
                    String published = jsonObject.getString("published");
                    String updated = jsonObject.getString("updated");
                    urlLink = jsonObject.getString("url");
                    selfLink = jsonObject.getString("selfLink");
                    String author = jsonObject.getJSONObject("author").getString("displayName");

                    titles.setVisibility(View.VISIBLE);
                    titles.setText(title);

                    Document doc = Jsoup.parse(content);
                    text = doc.text();


                     htmlContentWithStyle = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                            "<style>" +
                            "img, video { width: 95vw; height: auto; border-radius: 10px; object-fit: cover; }" +
                            "</style></head>" +
                            "<body>" + content + "</body></html>";


                    // Set a WebViewClient to handle page navigation inside the WebView
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                            // Get the URL of the clicked link
                            String url = request.getUrl().toString();

                            // Check if the URL is a YouTube video URL
                            if (url.contains("youtube.com") || url.contains("youtu.be")) {
                                // If it's a YouTube video URL, open it in the YouTube app or browser
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                                return true; // Tell the WebView that the URL is handled
                            } else {
                                // If it's not a YouTube video URL, open it in the device's web browser
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                                return true; // Tell the WebView that the URL is handled
                            }
                        }
                    });




                    //webView.loadData(desc,"text/html", ENCODING);
                    webView.loadDataWithBaseURL("", htmlContentWithStyle,"text/html","UTF-8","");
                    progressBar.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setVisibility(View.VISIBLE);
                            mayLike.setVisibility(View.VISIBLE);
                            postInfo.setVisibility(View.VISIBLE);
                            if(loves==0){
                                lovesPost.setText("0 loves");
                            }else {
                                lovesPost.setText(loves+" loves");
                            }

                            if(rateno==0){
                                reviewNoPost.setText("0 reviews");
                            }else {
                                reviewNoPost.setText(rateno+" reviews");
                            }

                            if(avgRate==0.0){
                                avgReviewPost.setText("No Rating History");
                            }else {
                                String formattedNumber = String.format("%.2f", avgRate);
                                avgReviewPost.setText(formattedNumber+" Stars");
                            }
                        }
                    },200);



                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    mayLike.setVisibility(View.VISIBLE);
                    throw new RuntimeException(e);


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response

                if (getSupportActionBar() != null) {
                    getSupportActionBar().hide();
                }
                progressBar.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                mayLike.setVisibility(View.VISIBLE);
                retrivePostData();
                Toast.makeText(PostWebViewActivity.this, "Something Problem or Internet Issue " , Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Post").child(key).child("views")
                        .setValue(views+1);
            }
        },30000);





    }

    private void retriveUserData() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo");
            databaseReference.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // User data found, retrieve the data
                         userName = snapshot.child("userName").getValue(String.class);
                         userImage = snapshot.child("userImage").getValue(String.class);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(FirebaseAuth.getInstance().getCurrentUser()!= null){
            FirebaseDatabase.getInstance().getReference("Post").child(key).child("favorite")
                    .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Boolean bool= (Boolean) snapshot.getValue();
                                //Toast.makeText(PostWebViewActivity.this, "Menu", Toast.LENGTH_SHORT).show();
                                Menu menu = toolbars.getMenu();; // Replace with your menu ID
                                MenuItem menuItem = menu.findItem(R.id.menu_post_favorite_border_id); // ID of the menu item to update

                                if (bool==true) {
                                    // Update the menu item properties
                                    menuItem.setTitle("Remove from Favorite");
                                    menuItem.setIcon(R.drawable.baseline_favorite_24);
                                } else if (bool==false) {
                                    menuItem.setTitle("Add to Favorite");
                                    menuItem.setIcon(R.drawable.baseline_favorite_border_24);
                                }else {
                                    menuItem.setTitle("Add to Favorite");
                                    menuItem.setIcon(R.drawable.baseline_favorite_border_24);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }

        if(item.getItemId() == R.id.menu_post_home_id) {
            Intent intent = new Intent(PostWebViewActivity.this, MainActivity.class);
            if(textToSpeech!=null){
                textToSpeech.stop();
            }
            intent.putExtra("val",2);
            startActivity(intent);
            return true; // Make sure to return true to indicate that the item click is handled
        }
        if(item.getItemId()==R.id.menu_post_share_id){

            try {
                if(urlLink != null){
                    Intent intent= new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String subject = "✔✔ " + title + ".\n\uD83D\uDC49\uD83D\uDC49 " + urlLink + "\n\n" +
                            "✔✔ I would like to share this app with you. Download this App from Google PlayStore.";
                    String appLink = "\uD83D\uDC49 \uD83D\uDC49"+"https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
                    String message = subject + "\n" + appLink;
                    intent.putExtra(Intent.EXTRA_TEXT, message);

                    startActivity(Intent.createChooser(intent,"Share With"));
                }else {
                    Toast.makeText(PostWebViewActivity.this, "Internet Connection Loss", Toast.LENGTH_SHORT).show();

                }

            }catch (Exception e){
                Toast.makeText(PostWebViewActivity.this, "Unable to Share!!!"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if(item.getItemId()==R.id.menu_post_view_site_id){

            if(urlLink != null){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(urlLink));
                 startActivity(intent);
            }else {
                Toast.makeText(PostWebViewActivity.this, "Internet Connection Loss", Toast.LENGTH_SHORT).show();

            }

        }
        if(item.getItemId()==R.id.menu_post_favorite_border_id){
            if(FirebaseAuth.getInstance().getCurrentUser() != null){
                FirebaseDatabase.getInstance().getReference("Post").child(key).child("favorite")
                        .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Boolean bool= (Boolean) snapshot.getValue();

                                    if(bool){
                                        FirebaseDatabase.getInstance().getReference("Post").child(key).child("favorite")
                                                .child(FirebaseAuth.getInstance().getUid()).setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(PostWebViewActivity.this, "Successfully remove from the Favorite List", Toast.LENGTH_SHORT).show();
                                                        FirebaseDatabase.getInstance().getReference("Post").child(key).child("postLoves").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                Long num=snapshot.getValue(Long.class);
                                                                if(snapshot.exists()){
                                                                    FirebaseDatabase.getInstance().getReference("Post").child(key).child("postLoves").setValue(num-1L);

                                                                }else {
                                                                    FirebaseDatabase.getInstance().getReference("Post").child(key).child("postLoves").setValue(1L);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                    }
                                                });

                                    }else {
                                        FirebaseDatabase.getInstance().getReference("Post").child(key).child("favorite")
                                                .child(FirebaseAuth.getInstance().getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(PostWebViewActivity.this, "Successfully added to the Favorite List", Toast.LENGTH_SHORT).show();
                                                        FirebaseDatabase.getInstance().getReference("Post").child(key).child("postLoves").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                Long num=snapshot.getValue(Long.class);
                                                                if(snapshot.exists()){
                                                                    FirebaseDatabase.getInstance().getReference("Post").child(key).child("postLoves").setValue(num+1L);

                                                                }else {
                                                                    FirebaseDatabase.getInstance().getReference("Post").child(key).child("postLoves").setValue(1L);
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
                                else {
                                    //Toast.makeText(PostWebViewActivity.this, "Not Exist", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(PostWebViewActivity.this, ""+key, Toast.LENGTH_SHORT).show();
                                    FirebaseDatabase.getInstance().getReference("Post").child(key).child("favorite")
                                            .child(FirebaseAuth.getInstance().getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(PostWebViewActivity.this, "Successfully added to the Favorite List", Toast.LENGTH_SHORT).show();
                                                    FirebaseDatabase.getInstance().getReference("Post").child(key).child("postLoves").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            Long num=snapshot.getValue(Long.class);
                                                            if(snapshot.exists()){
                                                                FirebaseDatabase.getInstance().getReference("Post").child(key).child("postLoves").setValue(num+1L);

                                                            }else {
                                                                FirebaseDatabase.getInstance().getReference("Post").child(key).child("postLoves").setValue(1L);
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
            else {
                doLogin(this);

            }


        }
        if(item.getItemId()==R.id.menu_post_rating_id){

            //check status
            checkStatus();

        }
        if(item.getItemId()==R.id.menu_post_speaker_id){

            Menu menu = toolbars.getMenu();
            MenuItem menuItem = menu.findItem(R.id.menu_post_speaker_id);
            if(htmlContentWithStyle!=null){

                if (textToSpeech != null && !text.isEmpty())
                {
                    if (bool==false) {
                        Toast.makeText(this, "Audio On", Toast.LENGTH_SHORT).show();

                        menuItem.setIcon(R.drawable.speaker_on);
                        bool=true;
                        textToSpeech.setSpeechRate(0.75f); // Adjust speech rate (0.5f is slower than normal)
                        textToSpeech.setPitch(1.0f); // Adjust pitch (1.0f is normal)
                        String[] sentences = text.split("[.!?]");
                        for (String sentence : sentences) {
                            // Trim whitespace and skip empty sentences
                            sentence = sentence.trim();
                            if (!sentence.isEmpty()) {
                                speakSentence(sentence);
                            }
                        }
                        // Start speaking
                        speakSentence(text);
                    }
                    else{
                        // Toggle to OFF state
                        menuItem.setIcon(R.drawable.speaker_off);
                        bool=false;
                        Toast.makeText(this, "Audio Off", Toast.LENGTH_SHORT).show();
                        textToSpeech.stop();
                    }


                }


            }

        }
        if(item.getItemId() == R.id.menu_post_different_language_id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PostWebViewActivity.this);
            View view = LayoutInflater.from(PostWebViewActivity.this).inflate(R.layout.convert_language, null);
            builder.setView(view);


            AlertDialog alertDialog1 = builder.create();
            Drawable drawable = ContextCompat.getDrawable(PostWebViewActivity.this, R.drawable.background);
            if (alertDialog1.getWindow() != null) {
                alertDialog1.getWindow().setBackgroundDrawable(drawable);
            }
            alertDialog1.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkStatus() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is signed in, proceed with rating
            doRating();
        } else {
            // No user is signed in, prompt login
            doLogin(this);
        }
    }
    public void doRating(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rating_design, null);
        builder.setView(dialogView);

        RatingBar ratingBar=dialogView.findViewById(R.id.ratingBar);
        EditText ratingText=dialogView.findViewById(R.id.rating_reason_id);
        TextView submit=dialogView.findViewById(R.id.rating_submit_id);

        //retrive
        FirebaseDatabase.getInstance().getReference("Post").child(key).child("rating")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
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

                if(!rating.isEmpty() && !ratingText.getText().toString().isEmpty()){

                    Calendar calendar = Calendar.getInstance();
                    Date times=calendar.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
                    String date = sdf.format(times);

                    Map<String,Object> map=new HashMap();
                    map.put("userid",FirebaseAuth.getInstance().getUid());
                    map.put("name",userName);
                    map.put("rate",Double.parseDouble(rating));
                    map.put("text",ratingText.getText().toString());
                    map.put("date",date);
                    map.put("image",userImage);

                    //retrive
                    FirebaseDatabase.getInstance().getReference("Post").child(key).child("ratingNum").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                FirebaseDatabase.getInstance().getReference("Post").child(key).child("rating")
                                        .child(FirebaseAuth.getInstance().getUid()).setValue(map);
                                Toast.makeText(PostWebViewActivity.this, "Successful", Toast.LENGTH_SHORT).show();

                                FirebaseDatabase.getInstance().getReference("Post").child(key).child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
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

                                        FirebaseDatabase.getInstance().getReference("Post").child(key).child("ratingNum").setValue(count);
                                        Double v1 = rate / count;
                                        FirebaseDatabase.getInstance().getReference("Post").child(key).child("avgRating").setValue(v1);
                                        alertDialog.dismiss();



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            else {
                                //add
                                FirebaseDatabase.getInstance().getReference("Post").child(key).child("rating")
                                        .child(FirebaseAuth.getInstance().getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(PostWebViewActivity.this, "Rating Successful", Toast.LENGTH_SHORT).show();

                                                FirebaseDatabase.getInstance().getReference("Post").child(key).child("ratingNum").setValue(1L);
                                                Double d=Double.parseDouble(rating);
                                                FirebaseDatabase.getInstance().getReference("Post").child(key).child("avgRating").setValue(d);
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
    public void doLogin(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);  // Use context to get LayoutInflater
        View dialogView = inflater.inflate(R.layout.log_design, null);
        builder.setView(dialogView);

        LinearLayout layout = dialogView.findViewById(R.id.google_login);

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        if (alertDialog.getWindow() != null) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.alert_back);
            alertDialog.getWindow().setBackgroundDrawable(drawable);
        }


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInHelper = new GoogleSignInHelper(PostWebViewActivity.this,alertDialog);
                mGoogleSignInHelper.signIn();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGoogleSignInHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void speakSentence(String sentence) {
        if (textToSpeech != null && !sentence.isEmpty()) {

            // Speak the sentence
            HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "text");
            textToSpeech.speak(sentence, TextToSpeech.QUEUE_ADD, params);
        }
    }
    private void retrivePostData() {

        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        postWebAdapter = new PostWebAdapter(PostWebViewActivity.this,list);

        int randomViewType = getRandomViewType(); // Implement your logic to get a random view type (1 or 2)
        postWebAdapter.setViewTypeToShow(randomViewType);
        if(randomViewType==1){
            recyclerView.setLayoutManager(new LinearLayoutManager(PostWebViewActivity.this));
        } else if (randomViewType==2) {
            recyclerView.setLayoutManager(new GridLayoutManager(PostWebViewActivity.this,1));
        }
        recyclerView.setAdapter(postWebAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MainPostModel mainPostModels= dataSnapshot.getValue(MainPostModel.class);
                    if (mainPostModels != null && mainPostModels.getLabel().equals(label)) {
                        list.add(mainPostModels);
                    }
                }

                Collections.shuffle(list);
                postWebAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    private int getRandomViewType() {
        Random random=new Random();
        int num=random.nextInt(2)+1;
        return num;
    }




}