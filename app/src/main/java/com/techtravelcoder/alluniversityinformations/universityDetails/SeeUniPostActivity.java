package com.techtravelcoder.alluniversityinformations.universityDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.FragmentModel.RatingModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SeeUniPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SeeUniPostAdapter seeUniPostAdapter;
    private ArrayList<RatingModel> list;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private ImageView imageView;
    private TextView textView,category;
    private String keyid,check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_uni_post);


        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.whiteTextSideColor1);
        }
        getWindow().setStatusBarColor(color);

        keyid=getIntent().getStringExtra("postid");
        check=getIntent().getStringExtra("checker");

        if(check.equals("1")){
            recyclerView=findViewById(R.id.see_uni_post_recycler_id);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            list=new ArrayList<>();
            seeUniPostAdapter=new SeeUniPostAdapter(this,list);
            seeUniPostAdapter.setViewTypeToShow(1);


            recyclerView.setAdapter(seeUniPostAdapter );
            databaseReference = FirebaseDatabase.getInstance().getReference("Post").child(keyid).child("rating");

            if(databaseReference!=null){

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            RatingModel ratingModel= dataSnapshot.getValue(RatingModel.class);
                            list.add(ratingModel);

                        }
                        seeUniPostAdapter.notifyDataSetChanged();

                        if(list.size()==0){
                            // Create an AlertDialog.Builder instance
                            AlertDialog.Builder builder = new AlertDialog.Builder(SeeUniPostActivity.this);

                            builder.setTitle(Html.fromHtml("<font color='#000000'>No Reviewers Foundℹ️</font>"));
                            builder.setMessage(Html.fromHtml("<font color='#000000'>ℹ️ We can not found any reviewers for this Content.❓❓</font>"));

                            builder.setCancelable(false) // Set whether the dialog can be canceled by tapping outside of it

                                    // Add a button to the dialog with a listener for when the button is clicked
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // Perform any actions needed when the "OK" button is clicked
                                            dialog.dismiss(); // Dismiss the dialog
                                        }
                                    });


                            // Create and show the AlertDialog
                            AlertDialog alert = builder.create();
                            alert.show();
                            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.alert_back);
                            alert.getWindow().setBackgroundDrawable(drawable);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SeeUniPostActivity.this, "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        if(check.equals("2")){
            recyclerView=findViewById(R.id.see_uni_post_recycler_id);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            list=new ArrayList<>();
            seeUniPostAdapter=new SeeUniPostAdapter(this,list);
            seeUniPostAdapter.setViewTypeToShow(1);


            recyclerView.setAdapter(seeUniPostAdapter );
            databaseReference = FirebaseDatabase.getInstance().getReference("University").child(keyid).child("rating");

            if(databaseReference!=null){

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            RatingModel ratingModel= dataSnapshot.getValue(RatingModel.class);
                            list.add(0,ratingModel);

                        }
                        seeUniPostAdapter.notifyDataSetChanged();

                        if(list.size()==0){
                            // Create an AlertDialog.Builder instance
                            AlertDialog.Builder builder = new AlertDialog.Builder(SeeUniPostActivity.this);

                            // Set the dialog title and message
                            builder.setTitle("No Reviewers Found")
                                    .setMessage("We can not found any reviewers for this University.")
                                    .setCancelable(false) // Set whether the dialog can be canceled by tapping outside of it

                                    // Add a button to the dialog with a listener for when the button is clicked
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // Perform any actions needed when the "OK" button is clicked
                                            dialog.dismiss(); // Dismiss the dialog
                                        }
                                    });

                            AlertDialog alert = builder.create();
                            alert.show();
                            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.alert_back);
                            alert.getWindow().setBackgroundDrawable(drawable);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SeeUniPostActivity.this, "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


    }
}