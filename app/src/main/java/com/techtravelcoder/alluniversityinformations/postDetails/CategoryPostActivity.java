package com.techtravelcoder.alluniversityinformations.postDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.FragmentAdapter.MainPostAdapter;
import com.techtravelcoder.alluniversityinformations.FragmentModel.MainPostModel;
import com.techtravelcoder.alluniversityinformations.mcq.QuestionTitleAdapter;
import com.techtravelcoder.alluniversityinformations.mcq.QuestionTitleModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class CategoryPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView,mcqRecyclerView;
    private MainPostAdapter mainPostAdapter;

    private ArrayList<MainPostModel> list;
    private DatabaseReference databaseReference;
    private SearchView searchView;
    private TextView cat;
    private String postId;
    private int sizeCheck=0;
    private String keyString;
    private ProgressBar progressBar;
    private TextView playListViews,playListRattings,playListItem,playListReviewNum;
    private Long countTotalViews=0L;
    private Boolean categoryType;
    private Double rating=0d;
    private int rateCnt=0;
    private Long reviewersNum=0l;
    private LinearLayout linearLayout,l1,l2;
    private Switch visible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_post);



        playListItem=findViewById(R.id.playlist_total_content_id);
        playListViews=findViewById(R.id.playlist_views_id);
        playListRattings=findViewById(R.id.playlist_rating_id);
        playListReviewNum=findViewById(R.id.playlist_review_id);
        l1=findViewById(R.id.ll_playlist_status_id);
        l2=findViewById(R.id.ll_question_set_id);



        progressBar=findViewById(R.id.category_post_progressbar_id);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.whiteTextColor1), PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.VISIBLE);

        visible=findViewById(R.id.switchButton);
        linearLayout=findViewById(R.id.ll_vcrr_id);

        visible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayout.setVisibility(View.GONE);
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                    mcqRecyclerView.setVisibility(View.GONE);

                    // Switch is turned on
                } else {
                    // Switch is turned off
                    linearLayout.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                    mcqRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });


        postId=getIntent().getStringExtra("id");

        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.whiteTextSideColor1);
        }
        getWindow().setStatusBarColor(color);
        keyString=getIntent().getStringExtra("title");
        categoryType=getIntent().getBooleanExtra("type",false);
        cat=findViewById(R.id.category_set_id);
        cat.setText(keyString);

        searchView=findViewById(R.id.category_post_searchView);
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(ContextCompat.getColor(CategoryPostActivity.this, android.R.color.black));
        editText.setHintTextColor(ContextCompat.getColor(CategoryPostActivity.this, R.color.allert_back_upper));

        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        mainPostAdapter = new MainPostAdapter(CategoryPostActivity.this,list,0);

        int randomViewType = getRandomViewType();
        mainPostAdapter.setViewTypeToShow(randomViewType);
        recyclerView = findViewById(R.id.category_post_recyclerview_id);
        if(randomViewType==1){
            recyclerView.setLayoutManager(new LinearLayoutManager(CategoryPostActivity.this));
        } else if (randomViewType==2) {
            recyclerView.setLayoutManager(new GridLayoutManager(CategoryPostActivity.this,1));

        }
        recyclerView.setAdapter(mainPostAdapter);

        // Fetch data from Firebase Database
        if(!keyString.equals("Most Popular Content")){
            fetchQuestionSetData();
        }

        fetchPostData();
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

    }

    private void fetchQuestionSetData() {
        mcqRecyclerView=findViewById(R.id.category_mcq_set_recyclerview_id);
        ArrayList<QuestionTitleModel>listMcq=new ArrayList<>();
        QuestionTitleAdapter questionTitleAdapter=new QuestionTitleAdapter(CategoryPostActivity.this,listMcq);
        mcqRecyclerView.setAdapter(questionTitleAdapter );
        mcqRecyclerView.setLayoutManager(new GridLayoutManager(CategoryPostActivity.this,1,RecyclerView.HORIZONTAL,false));

        FirebaseDatabase.getInstance().getReference("McqQuestion").child("QuestionSet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listMcq.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        QuestionTitleModel questionTitleModel = dataSnapshot.getValue(QuestionTitleModel.class);

                        if(questionTitleModel != null  && postId.equals(questionTitleModel.getUniqueNum())){
                            listMcq.add(0,questionTitleModel);
                        }

                    }

                }
                questionTitleAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void  searchList(String query) {
        List<MainPostModel> filteredList = new ArrayList<>();
        String queryWithoutSpaces = query.replaceAll("\\s+", "").toLowerCase(); // Remove spaces from query

        for (MainPostModel obj : list) {
            String objStringWithoutSpaces = obj.toString().replaceAll("\\s+", "").toLowerCase(); // Remove spaces from object

            if (objStringWithoutSpaces.contains(queryWithoutSpaces)) {
                filteredList.add(obj);
            }
        }

        // Update your UI with the filtered list
        mainPostAdapter.searchLists((ArrayList<MainPostModel>) filteredList);
    }


    private void fetchPostData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                   // Toast.makeText(CategoryPostActivity.this, ""+keyString, Toast.LENGTH_SHORT).show();
                    if(keyString.equals("Most Popular Content"))
                    {
                        MainPostModel mainPostModels= dataSnapshot.getValue(MainPostModel.class);
                        if (mainPostModels != null) {
                            list.add(mainPostModels);
                            countTotalViews+=mainPostModels.getViews();
                            sizeCheck++;
                            if(mainPostModels.getAvgRating() !=null){
                                rating=rating+mainPostModels.getAvgRating();
                                rateCnt++;

                            }
                            if(mainPostModels.getRatingNum()!=null){
                                reviewersNum=mainPostModels.getRatingNum()+reviewersNum;
                            }
                            if(sizeCheck>=200){
                                break;
                            }
                        }
                    }
                    else
                    {
                        MainPostModel mainPostModels= dataSnapshot.getValue(MainPostModel.class);
                        if (mainPostModels != null && mainPostModels.getUniqueNum().equals(postId)) {
                            list.add(mainPostModels);
                            countTotalViews+=mainPostModels.getViews();
                            if(mainPostModels.getAvgRating() !=null){
                                rating=rating+mainPostModels.getAvgRating();
                                rateCnt++;
                            }
                            if(mainPostModels.getRatingNum()!=null){
                                reviewersNum=mainPostModels.getRatingNum()+reviewersNum;
                            }
                        }
                    }

                }
                if(keyString.equals("Most Popular Content"))
                {
                    Collections.sort(list, new Comparator<MainPostModel>() {
                        @Override
                        public int compare(MainPostModel o1, MainPostModel o2) {
                            return Long.compare(o2.getViews(), o1.getViews()); // Descending
                        }
                    });

                    Double avg=rating/rateCnt;
                    String formatted = String.format("%.2f", avg);
                    playListRattings.setText(formatted+"");
                    playListViews.setText(String.valueOf(countTotalViews)+"");
                    playListItem.setText(String.valueOf(list.size())+"");
                    playListReviewNum.setText(reviewersNum+"");
                    mainPostAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                }
                else
                {

                    Double avg=rating/rateCnt;
                    String formatted = String.format("%.2f", avg);
                    playListRattings.setText(formatted+"");
                    playListReviewNum.setText(reviewersNum+"");

                    if(categoryType==true){
                        mainPostAdapter.notifyDataSetChanged();
                        playListViews.setText(String.valueOf(countTotalViews+""));
                        playListItem.setText(String.valueOf(list.size())+" ");
                        progressBar.setVisibility(View.GONE);
                        mainPostAdapter.notifyDataSetChanged();
                    }
                    else {
                        mainPostAdapter.notifyDataSetChanged();
                        playListViews.setText(String.valueOf(countTotalViews)+"");
                        playListItem.setText(String.valueOf(list.size())+"");
                        Collections.shuffle(list);
                        progressBar.setVisibility(View.GONE);
                        mainPostAdapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoryPostActivity.this, "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private int getRandomViewType() {
        Random random=new Random();
        int num=random.nextInt(2)+1;
        return num;
    }
}