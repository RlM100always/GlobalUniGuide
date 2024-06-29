package com.techtravelcoder.uniinfoadmin.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.uniinfoadmin.R;
import com.techtravelcoder.uniinfoadmin.country.CountryModel;
import com.techtravelcoder.uniinfoadmin.mcq.QuestionTitleAdapter;
import com.techtravelcoder.uniinfoadmin.mcq.QuestionTitleModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView,mcqRecyclerView;
    private FloatingActionButton floatingActionButton,mcqFloatButton;
    private EditText postId,imageLink,title;
    private String label,category,uniqueNum;
    private DatabaseReference mbase1;
    private ArrayList<MainPostModel>list;
    private MainPostModel mainPostModel;
    private MainPostAdapter mainPostAdapter;
    private androidx.appcompat.widget.SearchView searchView;
    private EditText edQuestonSet;
    private TextView addQuestionSet;

    private Long cnt=0L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_post);

        label=getIntent().getStringExtra("label");
        category=getIntent().getStringExtra("category");
        uniqueNum=getIntent().getStringExtra("num");


        recyclerView=findViewById(R.id.main_post_recycler_view_id);
        floatingActionButton=findViewById(R.id.main_post_float_button);
        mcqFloatButton=findViewById(R.id.mcq_float_button);

        retriveMcqTitle();


        mbase1 = FirebaseDatabase.getInstance().getReference("Post");

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        searchView=findViewById(R.id.searchViewMainPost);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });

        list=new ArrayList<>();
        mainPostAdapter=new MainPostAdapter(MainPostActivity.this,list,label,category);
        recyclerView.setAdapter(mainPostAdapter );

        mbase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        mainPostModel = dataSnapshot.getValue(MainPostModel.class);

                        if(mainPostModel != null  && uniqueNum.equals(mainPostModel.getUniqueNum())){
                            list.add(0,mainPostModel);
                        }

                    }

                }
                mainPostAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDialogue();
            }
        });
        mcqFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuestion();
            }
        });
    }

    private void setQuestion() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainPostActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.question_title_input,null);

        edQuestonSet=view.findViewById(R.id.ed_question_set_name);

        TextView addQuestionSet=view.findViewById(R.id.add_question_set_id);

        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();
        alertDialog.setCancelable(false);

        addQuestionSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edQuestonSet.getText().toString()) ){

                    String entryKey = FirebaseDatabase.getInstance().getReference("McqQuestion").push().getKey();

                    Map<String,Object> map=new HashMap<>();
                    map.put("title",edQuestonSet.getText().toString());
                    map.put("key",entryKey);
                    map.put("label",label);
                    map.put("uniqueNum",uniqueNum);
                    map.put("category",category);
                    FirebaseDatabase.getInstance().getReference("McqQuestion").child("QuestionSet").child(entryKey).setValue(map).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainPostActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                }
                            });



                    alertDialog.dismiss();

                }

            }
        });
    }

    private void postDialogue() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainPostActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.main_post_design,null);

        title=view.findViewById(R.id.post_title_id);
        imageLink=view.findViewById(R.id.post_image_link_id);
        postId=view.findViewById(R.id.post_post_id);

        TextView posttv=view.findViewById(R.id.add_main_post_id);


        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();
        alertDialog.setCancelable(false);

        posttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(imageLink.getText().toString()) && !TextUtils.isEmpty(postId.getText().toString())){

                    uploadData();

                   alertDialog.dismiss();

                }

            }
        });


    }
    private void uploadData() {
        Calendar calendar = Calendar.getInstance();
        Date times=calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
        String date = sdf.format(times);
        String entryKey = FirebaseDatabase.getInstance().getReference("Post").push().getKey();

        String s_title=title.getText().toString();
        String s_postid=postId.getText().toString();
        String s_link=imageLink.getText().toString();



        Map<String,Object> map=new HashMap<>();
        map.put("title",s_title);
        map.put("postId",s_postid);
        map.put("image",s_link);
        map.put("key",entryKey);
        map.put("label",label);
        map.put("uniqueNum",uniqueNum);
        map.put("category",category);
        map.put("date",date);
        map.put("views",cnt);


        //Toast.makeText(this, ""+s_title+" "+s_label+" "+entryKey+" "+s_link, Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference("Post").child(entryKey).setValue(map).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainPostActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void searchList(String newText) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MainPostModel> fList = new ArrayList<>();
                for (MainPostModel obj : list) {
                    //        String queryWithoutSpaces = query.replaceAll("\\s+", "").toLowerCase(); // Remove spaces from query
                    if (obj.getTitle().toLowerCase().replaceAll("\\s","").contains(newText.toLowerCase().trim().replaceAll("\\s",""))) {
                        fList.add(obj);
                    }
                }

                // Update the UI on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainPostAdapter.searchListsFunc((ArrayList<MainPostModel>) fList);
                        mainPostAdapter.notifyDataSetChanged();

                    }
                });
            }
        }).start();
    }

    private void retriveMcqTitle(){
        mcqRecyclerView=findViewById(R.id.question_set_recycler_id);
        ArrayList<QuestionTitleModel>listMcq=new ArrayList<>();
        QuestionTitleAdapter questionTitleAdapter=new QuestionTitleAdapter(MainPostActivity.this,listMcq);
        mcqRecyclerView.setAdapter(questionTitleAdapter );
        mcqRecyclerView.setLayoutManager(new GridLayoutManager(MainPostActivity.this,1,RecyclerView.HORIZONTAL,false));


        FirebaseDatabase.getInstance().getReference("McqQuestion").child("QuestionSet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listMcq.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        QuestionTitleModel questionTitleModel = dataSnapshot.getValue(QuestionTitleModel.class);

                        if(questionTitleModel != null  && uniqueNum.equals(questionTitleModel.getUniqueNum())){
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

}