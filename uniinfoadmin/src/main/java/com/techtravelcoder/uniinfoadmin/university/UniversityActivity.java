package com.techtravelcoder.uniinfoadmin.university;

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
import com.techtravelcoder.uniinfoadmin.country.CountryAdapter;
import com.techtravelcoder.uniinfoadmin.country.CountryModel;
import com.techtravelcoder.uniinfoadmin.country.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UniversityActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    private UniversityAdapter campainAdapter;
    private ArrayList<UniversityModel> list;
    UniversityModel campainModel;
    DatabaseReference mbase;

    FloatingActionButton floatingActionButton;
    EditText uniName,uniImage,uniLLink,top,priv,publ,sugg;
    TextView post;
    String contryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);

        contryName=getIntent().getStringExtra("name");

        mbase = FirebaseDatabase.getInstance().getReference("University");


        recyclerView=findViewById(R.id.recycler_view_university_id);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        list=new ArrayList<>();
        campainAdapter=new UniversityAdapter(this,list);
        recyclerView.setAdapter(campainAdapter );

        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        campainModel = dataSnapshot.getValue(UniversityModel.class);
                        //&& campainModel.getCountryName().equals(contryName)


                        if(campainModel != null  && campainModel.getContryName() != null&& campainModel.getContryName().equals(contryName)){
                            list.add(0,campainModel);

                        }

                    }

                }
                campainAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        floatingActionButton=findViewById(R.id.uni_float_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allertOn();
            }
        });
    }

    private void allertOn() {
        AlertDialog.Builder builder=new AlertDialog.Builder(UniversityActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.university_input,null);

        uniName=view.findViewById(R.id.university_name_id);
        uniImage=view.findViewById(R.id.university_image_id);
        uniLLink=view.findViewById(R.id.university_website_id);
        top=view.findViewById(R.id.top_id);
        sugg=view.findViewById(R.id.suggest_id);
        priv=view.findViewById(R.id.private_id);
        publ=view.findViewById(R.id.public_id);
        post=view.findViewById(R.id.add_university_post_id);


        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(uniName.getText().toString()) && !TextUtils.isEmpty(uniImage.getText().toString()) && !TextUtils.isEmpty(uniLLink.getText().toString())
                && !TextUtils.isEmpty(priv.getText().toString()) && !TextUtils.isEmpty(publ.getText().toString()) && !TextUtils.isEmpty(sugg.getText().toString()) && !TextUtils.isEmpty(top.getText().toString()) ){
                    uploadData();
                    alertDialog.dismiss();

                }

            }
        });
    }

    private void uploadData() {

        String entryKey = FirebaseDatabase.getInstance().getReference("University").push().getKey();
        String s_name=uniName.getText().toString();
        String s_link=uniLLink.getText().toString();
        String s_image=uniImage.getText().toString();
        String prv=priv.getText().toString();
        String pub=publ.getText().toString();
        String sug=sugg.getText().toString();
        String tp=top.getText().toString();
        Map<String,Object> map=new HashMap<>();
        map.put("contryName",contryName);
        map.put("uniWebLink",s_link);
        map.put("uniName",s_name);
        map.put("uniImageLink",s_image);
        map.put("privates",prv);
        map.put("publics",pub);
        map.put("best",tp);
        map.put("suggested",sug);
        map.put("key",entryKey);



        FirebaseDatabase.getInstance().getReference("University").child(entryKey).setValue(map).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UniversityActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}