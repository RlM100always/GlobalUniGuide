package com.techtravelcoder.alluniversityinformations.mcq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.countryDetails.MainActivity;
import com.techtravelcoder.alluniversityinformations.postDetails.CategoryPostActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DoExamActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<DoEaxmModel> list;
    private DoEaxmModel doEaxmModel;
    private String key;
    int MCQ_LIMIT=10;
    private TextView examTopic,exammQuesTionSet,totalQues,examSubmit;
    private String title,category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exam);
        examTopic=findViewById(R.id.exam_topic_id);
        exammQuesTionSet=findViewById(R.id.exam_question_set_id);
        totalQues=findViewById(R.id.exam_question_number_id);
        examSubmit=findViewById(R.id.question_submit_id);

        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.whiteTextSideColor1);
        }
        getWindow().setStatusBarColor(color);

        Toast.makeText(this, "Exam Started", Toast.LENGTH_LONG).show();



        key=getIntent().getStringExtra("key");
        title=getIntent().getStringExtra("title");
        category=getIntent().getStringExtra("category");
        examTopic.setText("Topic : "+category);
        exammQuesTionSet.setText("Set : "+title);
        if(key !=null && FirebaseAuth.getInstance().getUid()!=null){
            FirebaseDatabase.getInstance().getReference("ExamHis").child(FirebaseAuth.getInstance().getUid())
                    .child(key).child("CurrentExam").removeValue();

            FirebaseDatabase.getInstance().getReference("ExamHis").child(FirebaseAuth.getInstance().getUid())
                    .child(key).child("status").setValue("false");
        }




        examSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });

        retriveMcqQuestion();
    }

    private void calculate() {
        ArrayList<DoEaxmModel>newList=new ArrayList<>();

        int checkedCount = 0;
        int uncheckedCount = 0;

        for (DoEaxmModel question : list) {
            if (question.isAnswered()) {
                checkedCount++;
            } else {
                uncheckedCount++;
            }
        }


        if(list.size()==checkedCount  && list.size()!=0){
            Calendar calendar = Calendar.getInstance();
            Date times=calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
            String date = sdf.format(times);

            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("ExamHis").child(FirebaseAuth.getInstance().getUid());
            databaseReference.child(key).child("title").setValue(title);
            databaseReference.child(key).child("key").setValue(key);
            databaseReference.child(key).child("category").setValue(category);
            databaseReference.child(key).child("date").setValue(date);
            databaseReference.child(key).child("status").setValue("true");

            showPopUp();



        }
        else {
            Toast.makeText(this, "Fill up All Question", Toast.LENGTH_SHORT).show();
        }



    }

    private void showPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DoExamActivity.this);
        LayoutInflater inflater = LayoutInflater.from(DoExamActivity.this);  // Use context to get LayoutInflater
        View dialogView = inflater.inflate(R.layout.mcq_result_pop_up, null);
        builder.setView(dialogView);

        TextView seeResult=dialogView.findViewById(R.id.exam_see_result_id);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);

        alertDialog.show();
        if (alertDialog.getWindow() != null) {
            Drawable drawable = ContextCompat.getDrawable(DoExamActivity.this, R.drawable.alert_back);
            alertDialog.getWindow().setBackgroundDrawable(drawable);
        }

        seeResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DoExamActivity.this,DetailsExamHisActivity.class);
                intent.putExtra("key",key);
                startActivity(intent);
                finish();
            }
        });
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

                if(snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        doEaxmModel = dataSnapshot.getValue(DoEaxmModel.class);

                        if(doEaxmModel != null){
                            allQuestions.add(doEaxmModel);
                        }
                        // Shuffle the list to randomize the questions
                        Collections.shuffle(allQuestions);
                        list.clear();


                        // Trim the list to the first 15 items if it contains more than 15 items
                        if (allQuestions.size() > MCQ_LIMIT) {
                            list.addAll(allQuestions.subList(0, MCQ_LIMIT));
                        }
                        else {
                            list.addAll(allQuestions);
                        }


                    }
                    totalQues.setText("Total Question : "+list.size());

                }
                else {
                    totalQues.setText("Total Question : 0");

                }
                doExamAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        backPopUp();
        Toast.makeText(this, "You are in the exam moment", Toast.LENGTH_SHORT).show();
    }

    private void backPopUp() {
        AlertDialog.Builder alertObj = new AlertDialog.Builder(DoExamActivity.this);
        alertObj.setTitle(Html.fromHtml("<font color='#000000'>Cancel Exam !!!!️</font>"));
        alertObj.setMessage(Html.fromHtml("<font color='#000000'>Do you want to cancel this Exam ??</font>"));

        alertObj.setPositiveButton(Html.fromHtml("<font color='#000000'>✅Yes</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close the activity when 'Yes' is clicked
                Toast.makeText(DoExamActivity.this, "Exam Cancelled Successfully", Toast.LENGTH_SHORT).show();
                DoExamActivity.super.onBackPressed();
            }
        });

        alertObj.setNegativeButton(Html.fromHtml("<font color='#000000'>❌No</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog when 'No' is clicked
                dialog.cancel();
            }
        });

        AlertDialog dialog = alertObj.create();
        dialog.show();

        // Set the background drawable for the dialog
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.alert_back);
        if (drawable != null) {
            dialog.getWindow().setBackgroundDrawable(drawable);
        }
    }



    }