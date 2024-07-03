package com.techtravelcoder.alluniversityinformations.mcq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.books.BookCategoryActivity;
import com.techtravelcoder.alluniversityinformations.books.BookCategoryAdapter;
import com.techtravelcoder.alluniversityinformations.books.BookCategoryModel;

import java.util.ArrayList;

public class ExamHisActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ExamDetails>list;
    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_his);

        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.whiteTextSideColor1);
        }
        getWindow().setStatusBarColor(color);

        recyclerView=findViewById(R.id.exam_his_recycler_view_id);
        imageView=findViewById(R.id.exam_his_favorite_image);
        textView=findViewById(R.id.exam_his_favorite_text_id);


        retriveExamHistory();
    }

    private void retriveExamHistory() {
        list=new ArrayList<>();
        ExamHisAdapter examHisAdapter=new ExamHisAdapter(ExamHisActivity.this,list);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView.setAdapter(examHisAdapter);


        FirebaseDatabase.getInstance().getReference("ExamHis")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ExamDetails examDetails=dataSnapshot.getValue(ExamDetails.class);

                        if (examDetails!=null && examDetails.getTitle()!=null  && examDetails.getStatus()!=null) {
                            if(examDetails.getStatus().equals("true")){
                                list.add(examDetails);
                            }
                        }

                    }
                    if(list.size()==0){
                        imageView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);

                    }

                }
                examHisAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}