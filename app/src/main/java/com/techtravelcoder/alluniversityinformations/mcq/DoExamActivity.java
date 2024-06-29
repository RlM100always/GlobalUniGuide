package com.techtravelcoder.alluniversityinformations.mcq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.postDetails.CategoryPostActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoExamActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<DoEaxmModel> list;
    private String key;
    int MCQ_LIMIT=15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exam);
        key=getIntent().getStringExtra("key");
        retriveMcqQuestion();
    }

    private void retriveMcqQuestion() {
        list=new ArrayList<>();
        recyclerView=findViewById(R.id.exam_recycler_view_id);
        DoExamAdapter doExamAdapter=new DoExamAdapter(DoExamActivity.this,list,key);
        recyclerView.setAdapter(doExamAdapter );
        recyclerView.setLayoutManager(new GridLayoutManager(DoExamActivity.this,1,RecyclerView.VERTICAL,false));
        FirebaseDatabase.getInstance().getReference("McqQuestion").child("QuestionSet")
                .child(key).child("AllQuestion")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DoEaxmModel> allQuestions = new ArrayList<>();

                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        DoEaxmModel doEaxmModel = dataSnapshot.getValue(DoEaxmModel.class);

                        if(doEaxmModel != null){
                            allQuestions.add(doEaxmModel);
                        }
                        // Shuffle the list to randomize the questions
                        Collections.shuffle(allQuestions);
                        list.clear();


                        // Trim the list to the first 15 items if it contains more than 15 items
                        if (allQuestions.size() > MCQ_LIMIT) {
                            list.addAll(allQuestions.subList(0, MCQ_LIMIT));
                        } else {
                            list.addAll(allQuestions);
                        }

                    }

                }
                doExamAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}