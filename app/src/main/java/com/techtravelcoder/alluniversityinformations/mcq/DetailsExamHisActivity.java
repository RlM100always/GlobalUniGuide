package com.techtravelcoder.alluniversityinformations.mcq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailsExamHisActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<DetailsExamHisModel>list;
    private DetailsExamHisModel detailsExamHisModel;
    private String key;
    private TextView tques,cans,rans,cp,rp;
    private int cnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_exam_his);

        recyclerView=findViewById(R.id.exam_result_recyclerview_id);
        tques=findViewById(R.id.total_ques_id);
        cans=findViewById(R.id.total_correct_id);
        rans=findViewById(R.id.total_wrong_id);
        cp=findViewById(R.id.correct_percentage_id);
        rp=findViewById(R.id.wrong_percentage_id);

        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.whiteTextSideColor1);
        }
        getWindow().setStatusBarColor(color);

        key=getIntent().getStringExtra("key");

        if(key!=null && FirebaseAuth.getInstance().getUid() != null){
            retriveResult();
        }


    }

    private void retriveResult() {

        list=new ArrayList<>();
        DetailsExamHisAdapter detailsExamHisAdapter=new DetailsExamHisAdapter(DetailsExamHisActivity.this,list);
        recyclerView.setAdapter(detailsExamHisAdapter );
        recyclerView.setLayoutManager(new GridLayoutManager(DetailsExamHisActivity.this,1,RecyclerView.VERTICAL,false));
        FirebaseDatabase.getInstance().getReference("ExamHis").child(FirebaseAuth.getInstance().getUid())
                .child(key).child("CurrentExam")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();

                        if(snapshot.exists())
                        {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                detailsExamHisModel = dataSnapshot.getValue(DetailsExamHisModel.class);

                                if(detailsExamHisModel != null){
                                    list.add(detailsExamHisModel);
                                }
                                if(detailsExamHisModel != null && detailsExamHisModel.getAnsStatus().equals("true")){
                                    cnt+=1;
                                }
                            }
                            tques.setText("Total Question : "+list.size());
                            cans.setText("Correct Answer : "+cnt);
                            rans.setText("Wrong Answer : "+(list.size()-cnt));
                            double correctPercentage = ((double) cnt / list.size()) * 100;
                            double wrongPercentage = 100 - correctPercentage;
                            String formattedCorrectPercentage = String.format("%.2f", correctPercentage);
                            String formattedWrongPercentage = String.format("%.2f", wrongPercentage);

                            cp.setText("Correct: " + formattedCorrectPercentage+" % ");
                            rp.setText("Wrong: " + formattedWrongPercentage+" % ");

                        }
                        detailsExamHisAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}