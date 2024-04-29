package com.techtravelcoder.uniinfoadmin.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private EditText postId,imageLink,title;
    private String label,category,uniqueNum;
    private DatabaseReference mbase1;
    private ArrayList<MainPostModel>list;
    private MainPostModel mainPostModel;
    private MainPostAdapter mainPostAdapter;

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


        mbase1 = FirebaseDatabase.getInstance().getReference("Post");

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));


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

}