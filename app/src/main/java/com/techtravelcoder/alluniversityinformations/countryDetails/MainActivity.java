package com.techtravelcoder.alluniversityinformations.countryDetails;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.startapp.sdk.ads.banner.Banner;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.FragmentAdapter.MainPostAdapter;
import com.techtravelcoder.alluniversityinformations.FragmentModel.MainPostModel;
import com.techtravelcoder.alluniversityinformations.ads.ADSSetUp;
import com.techtravelcoder.alluniversityinformations.ads.GoogleSignInHelper;
import com.techtravelcoder.alluniversityinformations.books.BookCategoryActivity;
import com.techtravelcoder.alluniversityinformations.books.BookCategoryAdapter;
import com.techtravelcoder.alluniversityinformations.books.BookCategoryModel;
import com.techtravelcoder.alluniversityinformations.books.BookPostActivity;
import com.techtravelcoder.alluniversityinformations.books.BookPostAdapter;
import com.techtravelcoder.alluniversityinformations.books.BookPostModel;
import com.techtravelcoder.alluniversityinformations.mcq.ExamHisActivity;
import com.techtravelcoder.alluniversityinformations.notes.AddFriendsActivity;
import com.techtravelcoder.alluniversityinformations.pdf.DatabaseHelper;
import com.techtravelcoder.alluniversityinformations.pdf.FileDelete;
import com.techtravelcoder.alluniversityinformations.postDetails.CategoryPostActivity;
import com.techtravelcoder.alluniversityinformations.postDetails.PostHandleActivity;
import com.techtravelcoder.alluniversityinformations.universityDetails.ReBookMarkActivity;
import com.techtravelcoder.alluniversityinformations.vocabulary.VocabularyActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private RecyclerView recyclerView ,difBookCategoryRecyclerView,suggestedBooksRecyclerview;
    private BookCategoryAdapter bookCategoryAdapter;
    private BookPostAdapter bookPostAdapter;

    private CountryAdapter campainAdapter;
    private ArrayList<CountryModel> list;
    private ArrayList<BookPostModel> bookList;
    private ArrayList<BookCategoryModel> bookCategoryList;
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
    private AlertDialog alertDialog;
    private GoogleSignInHelper mGoogleSignInHelper;
    private TextView notice;
    private ProgressBar progressBar,progressBar1,progressBar2,progressBar3;
    private LinearLayout visit,recentUni,carrierGuide,popularContent,bookUni,dictionary,bookCollection,newBook,bookMarkBook;
    private LinearLayout examHis,addfriends,addNotes;




    //new data

    private RecyclerView recyclerViewNew;
    private MainPostAdapter mainPostAdapter;
    private ArrayList<MainPostModel> listNew;
    private DatabaseReference databaseReferenceNew;

    private LinearLayout userProf;
    private ImageView userImg;
    private TextView userNme,markText;
    private static final int REQUEST_USE_FULL_SCREEN_INTENT = 1;
    private ImageSlider imageSlider;
    private CardView cardView;
    private DatabaseHelper databaseHelper;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //for ads disable
//        StartAppSDK.init(this, "201407686");
//        StartAppAd.disableAutoInterstitial();


        notice=findViewById(R.id.notice_id_main);
        userProf=findViewById(R.id.ll_user_profile);
        userImg=findViewById(R.id.user_image_id);
        userNme=findViewById(R.id.user_name_id);
        cardView=findViewById(R.id.pimage_card_id);
        addNotes=findViewById(R.id.add_notes_id);
        databaseHelper = new DatabaseHelper(MainActivity.this);



        //profile handelling
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            userProf.setVisibility(View.VISIBLE);
            FirebaseDatabase.getInstance().getReference("UserInfo").child(FirebaseAuth.getInstance().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String name = (String) snapshot.child("userName").getValue();
                                String image = (String) snapshot.child("userImage").getValue();
                                userNme.setText(name);
                                Glide.with(getApplicationContext()).load(image).into(userImg);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            userProf.setVisibility(View.VISIBLE);

        }
        else {
            userProf.setVisibility(View.GONE);
        }








        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.whiteTextSideColor1);
        }
        getWindow().setStatusBarColor(color);




        gridLayoutManager=new GridLayoutManager(MainActivity.this,2,GridLayoutManager.HORIZONTAL,false);
        progressBar=findViewById(R.id.progressBar_id);
        recyclerView=findViewById(R.id.main_recycler_id);
        recyclerViewNew=findViewById(R.id.newdata_recyclerview_id);
        difBookCategoryRecyclerView=findViewById(R.id.different_book_category_recycler_id);
        examHis=findViewById(R.id.my_exam_history_id);
        addfriends=findViewById(R.id.add_friends_id);

        imageSlider=findViewById(R.id.image_slider);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh_layout_id);
        drawerLayout=findViewById(R.id.drawer_id);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.tolbar);
        recentUni=findViewById(R.id.recent_uni_id);
        carrierGuide=findViewById(R.id.carrier_guide_id);
        popularContent=findViewById(R.id.popular_content_id);
        bookUni=findViewById(R.id.bookmark_uni_id);
        dictionary=findViewById(R.id.dictionary_id);
        bookCollection=findViewById(R.id.book_collection_id);
        newBook=findViewById(R.id.new_book_id);
        bookMarkBook=findViewById(R.id.my_bookmark_book_id);
        progressBar1=findViewById(R.id.progressBar_id1);
        progressBar2=findViewById(R.id.progressBar_id2);
        progressBar3=findViewById(R.id.progressBar_id3);
        markText=findViewById(R.id.marque_text_id);
        imageSlider = findViewById(R.id.image_slider);

        //handle marque text
        FirebaseDatabase.getInstance().getReference("Ads Control").child("hStatus")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    String ans=snapshot.getValue(String.class);
                                    if(ans.equals("on")){
                                        notice.setVisibility(View.VISIBLE);
                                        FirebaseDatabase.getInstance().getReference("Ads Control").
                                                child("marque").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            String val=snapshot.child("text").getValue(String.class);
                                                            markText.setVisibility(View.VISIBLE);
                                                            markText.setText(val);
                                                            markText.setSelected(true);


                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });




        //slider call
        try {
            FirebaseDatabase.getInstance().getReference("Ads Control")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String ans11=snapshot.child("pStatus").getValue(String.class);
                                Toast.makeText(MainActivity.this, ""+ans11, Toast.LENGTH_SHORT).show();
                                if(ans11.equals("on")){
                                    cardView.setVisibility(View.VISIBLE);
                                    sliderSupport();
                                }else {
                                }
                            }else {

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }catch (Exception e){
        }




        // Initialize FirebaseAuth instance

        mbase = FirebaseDatabase.getInstance().getReference("Country");
        mbase.keepSynced(true);




        progressBar.setVisibility(View.VISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.whiteTextColor1), PorterDuff.Mode.SRC_IN);

        progressBar1.setVisibility(View.VISIBLE);
        progressBar1.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.whiteTextColor1), PorterDuff.Mode.SRC_IN);

        progressBar2.setVisibility(View.VISIBLE);
        progressBar2.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.whiteTextColor1), PorterDuff.Mode.SRC_IN);

        progressBar3.setVisibility(View.VISIBLE);
        progressBar3.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.whiteTextColor1), PorterDuff.Mode.SRC_IN);





        examHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getUid()!=null){
                    Intent intent=new Intent(getApplicationContext(),ExamHisActivity.class);
                    startActivity(intent);
                }
                else {
                    doLogin(MainActivity.this);
                }
            }
        });

        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getUid()!=null){
                    Intent intent=new Intent(getApplicationContext(), AddFriendsActivity.class);
                    intent.putExtra("check","2");
                    startActivity(intent);
                }
                else {
                    doLogin(MainActivity.this);
                }
            }
        });

        addfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getUid()!=null){
                    Intent intent=new Intent(getApplicationContext(), AddFriendsActivity.class);
                    intent.putExtra("check","1");
                    startActivity(intent);
                }
                else {
                    doLogin(MainActivity.this);
                }
            }
        });


        bookMarkBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    Intent intent=new Intent(getApplicationContext(), BookPostActivity.class);
                    intent.putExtra("key","@");
                    startActivity(intent);
                }
                else {
                    doLogin(MainActivity.this);
                }
            }
        });
        newBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    Intent intent=new Intent(getApplicationContext(), BookPostActivity.class);
                    intent.putExtra("key","@b");

                    startActivity(intent);
                }
                else {
                    doLogin(MainActivity.this);
                }

            }
        });

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
                if(FirebaseAuth.getInstance().getUid()!=null){
                    Intent intent=new Intent(MainActivity.this, PostHandleActivity.class);
                    intent.putExtra("ggg",44);
                    ADSSetUp.adsType1(MainActivity.this);
                    startActivity(intent);
                }
                else {
                    doLogin(MainActivity.this);
                }


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

                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    Intent intent=new Intent(MainActivity.this, ReBookMarkActivity.class);
                    ADSSetUp.adsType1(MainActivity.this);
                    intent.putExtra("check",2);
                    startActivity(intent);

                }else {
                    doLogin(MainActivity.this);
                }

            }
        });
        dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),VocabularyActivity.class);
                startActivity(intent);
            }
        });
        bookCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    Intent intent=new Intent(getApplicationContext(), BookCategoryActivity.class);
                    startActivity(intent);
                }
                else {
                    doLogin(MainActivity.this);
                }

            }
        });


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
        Runnable loadDataTask = () -> {
            loadData();
        };

        Runnable refreshDataTask = () -> {
            refeshData();
        };

        Runnable fetchNewPostDataTask = () -> {
            fetchNewPostData();
        };

        Runnable fetchBookCategoryTask = () -> {
            fetchBookCategory();
        };

        Runnable retrieveDifferent50BooksTask = () -> {
            retriveDiffernt50Books();
        };

        // Create threads with the tasks
        Thread loadDataThread = new Thread(loadDataTask);
        Thread refreshDataThread = new Thread(refreshDataTask);
        Thread fetchNewPostDataThread = new Thread(fetchNewPostDataTask);
        Thread fetchBookCategoryThread = new Thread(fetchBookCategoryTask);
        Thread retrieveDifferent50BooksThread = new Thread(retrieveDifferent50BooksTask);

        // Start all threads
        loadDataThread.start();
        refreshDataThread.start();
        if(FirebaseAuth.getInstance().getUid()!=null){
            fetchBookCategoryThread.start();
            retrieveDifferent50BooksThread.start();

        }else {
            progressBar1.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
        }
        fetchNewPostDataThread.start();


        // Wait for all threads to finish
        try {
            loadDataThread.join();
            refreshDataThread.join();
            fetchNewPostDataThread.join();
            if(FirebaseAuth.getInstance().getUid()!=null){
                fetchBookCategoryThread.join();
                retrieveDifferent50BooksThread.join();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }





        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Drawable navIcon = toolbar.getNavigationIcon();
        if (navIcon != null) {
            navIcon.setColorFilter(getResources().getColor(android.R.color.holo_green_dark), PorterDuff.Mode.SRC_IN);
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refeshData();
                fetchNewPostData();
                if(FirebaseAuth.getInstance().getUid()!=null){
                    fetchBookCategory();
                    retriveDiffernt50Books();
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        navItemOnClickListener();




    }

    public void sliderSupport() {
        final List<SlideModel> remoteimages = new ArrayList<>(); // SlideModel is an inbuilt model class made by the GitHub library provider
        List<String> remoteimages1 = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Ads Control").child("Premium ads")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                remoteimages.add(new SlideModel(
                                        data.child("image").getValue().toString(),
                                        data.child("title").getValue().toString(),
                                        ScaleTypes.FIT));
                              //  Toast.makeText(MainActivity.this, ""+data.child("siteUrl").getValue(String.class),Toast.LENGTH_SHORT).show();
                                remoteimages1.add((String) data.child("siteUrl").getValue());
                            }
                            // Set the image list and click listener outside the loop
                            imageSlider.setImageList(remoteimages, ScaleTypes.FIT);
                            imageSlider.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemSelected(int i) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(String.valueOf(remoteimages1.get(i))));
                                    startActivity(intent);
                                }

                                @Override
                                public void doubleClick(int i) {
                                    // Handle double click if needed
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error if needed
                    }
                });
    }










    private void retriveDiffernt50Books() {
        suggestedBooksRecyclerview=findViewById(R.id.different_book_recycler_id);
        suggestedBooksRecyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(),1,GridLayoutManager.HORIZONTAL,false));
        bookList = new ArrayList<>();
        bookPostAdapter = new BookPostAdapter(this, bookList, 4);

        suggestedBooksRecyclerview.setAdapter(bookPostAdapter);
        FirebaseDatabase.getInstance().getReference("Book Details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                List<BookPostModel> allBooks = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        BookPostModel bookPostModel = dataSnapshot.getValue(BookPostModel.class);
                        if (bookPostModel != null) {
                            allBooks.add(bookPostModel);
                        }
                    }

                    // Shuffle the list of all books
                    Collections.shuffle(allBooks);
                    progressBar2.setVisibility(View.GONE);


                    // Take the first 50 items or fewer if the list size is less than 50
                    int itemsToFetch = Math.min(40, allBooks.size());
                    for (int i = 0; i < itemsToFetch; i++) {
                        bookList.add(allBooks.get(i));
                    }

                    bookPostAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchBookCategory() {
        bookCategoryList = new ArrayList<>();
        bookCategoryAdapter = new BookCategoryAdapter(MainActivity.this, bookCategoryList);
        difBookCategoryRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1,GridLayoutManager.HORIZONTAL,false));
        difBookCategoryRecyclerView.setAdapter(bookCategoryAdapter);

        FirebaseDatabase.getInstance().getReference("Book Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BookCategoryModel> allBooks = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        BookCategoryModel bookCategoryModel = dataSnapshot.getValue(BookCategoryModel.class);
                        if (bookCategoryModel != null) {
                            allBooks.add(bookCategoryModel);
                        }
                    }
                }

                // Shuffle the list
                Collections.shuffle(allBooks);

                // Clear the current list
                bookCategoryList.clear();
                progressBar1.setVisibility(View.GONE);


                // Add up to 20 items to the bookCategoryList
                for (int i = 0; i < Math.min(15, allBooks.size()); i++) {
                    bookCategoryList.add(allBooks.get(i));
                }

                // Notify the adapter
                bookCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Log.e("MainActivity", "Database error: " + error.getMessage());
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGoogleSignInHelper.onActivityResult(requestCode, resultCode, data);
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
                mGoogleSignInHelper = new GoogleSignInHelper(MainActivity.this,alertDialog);
                mGoogleSignInHelper.signIn();
            }
        });
    }



    private void refeshData() {
       // recyclerView.setLayoutManager(gridLayoutManager);
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

                    if(FirebaseAuth.getInstance().getUid()!=null){
                        Intent openWebsiteIntent = new Intent(MainActivity.this, PostHandleActivity.class);
                        ADSSetUp.adsType1(MainActivity.this);
                        startActivity(openWebsiteIntent);
                    }
                    else {
                        doLogin(MainActivity.this);
                    }


                }

                if(item.getItemId()==R.id.menu_privacy_id){

                    String websiteUrl = "https://newsfeed420s.blogspot.com/p/privacy-policy.html";
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
                    ADSSetUp.adsType1(MainActivity.this);
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
                    if(FirebaseAuth.getInstance().getCurrentUser() != null){
                        Intent intent=new Intent(MainActivity.this, ReBookMarkActivity.class);
                        intent.putExtra("check",2);
                        ADSSetUp.adsType1(MainActivity.this);
                        startActivity(intent);
                    }
                    else {
                        doLogin(MainActivity.this);
                    }


                }
                if(item.getItemId()==R.id.menu_recent_id){
                    Intent intent=new Intent(MainActivity.this, ReBookMarkActivity.class);
                    intent.putExtra("check",1);
                    ADSSetUp.adsType1(MainActivity.this);
                    startActivity(intent);

                }
                if(item.getItemId()==R.id.menu_vocabulayr_id){
                    Intent intent=new Intent(MainActivity.this, VocabularyActivity.class);
                    ADSSetUp.adsType1(MainActivity.this);
                    startActivity(intent);

                }
                //menu_exam_his_id
                if(item.getItemId()==R.id.menu_exam_his_id){
                    if(FirebaseAuth.getInstance().getUid()!=null){
                        Intent intent=new Intent(MainActivity.this, ExamHisActivity.class);
                        startActivity(intent);
                    }
                    else {
                        doLogin(MainActivity.this);
                    }

                }
                if(item.getItemId()==R.id.menu_free_id){

                    FileDelete.deleteAllDownloadedFiles(MainActivity.this, new FileDelete.DeletionListener() {
                        @Override
                        public void onDeletionComplete() {
                            // Delete all records from the database
                            databaseHelper.deleteAllFiles();


                            // Show a toast message
                            Toast.makeText(MainActivity.this, "Successfully Reduce App Size", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onDeletionFailed(Exception e) {
                            // Show a toast message
                            Toast.makeText(MainActivity.this, "Failed to delete files: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //menu_update_app_id
                if(item.getItemId()==R.id.menu_update_app_id){
                    Uri uri=Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());

                    Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                    try {
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Unable to ratting !!!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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
    private void fetchNewPostData() {
        listNew = new ArrayList<>();
        mainPostAdapter = new MainPostAdapter(getApplicationContext(), listNew,1);

        databaseReferenceNew = FirebaseDatabase.getInstance().getReference("Post");
        mainPostAdapter.setViewTypeToShow(2);
        recyclerViewNew.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNew.setAdapter(mainPostAdapter);


        databaseReferenceNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listNew.clear();
                List<MainPostModel> allPosts = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MainPostModel mainPostModel = dataSnapshot.getValue(MainPostModel.class);
                    if (mainPostModel != null) {
                        allPosts.add(mainPostModel);
                    }
                }

                // Shuffle the list of all posts
                Collections.shuffle(allPosts);
                progressBar3.setVisibility(View.GONE);


                // Take the first 120 items or fewer if the list size is less than 120
                int itemsToFetch = Math.min(150, allPosts.size());
                for (int i = 0; i < itemsToFetch; i++) {
                    listNew.add(allPosts.get(i));
                }

                mainPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}