package com.techtravelcoder.uniinfoadmin.university;

import android.app.Notification;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techtravelcoder.uniinfoadmin.R;
import com.techtravelcoder.uniinfoadmin.country.CountryModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    private SearchView searchView;
    private String checkedTextTop,checkedTextPublic,checkedTextPrivates,checkedTextSuggest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);

        contryName=getIntent().getStringExtra("name");
        searchView=findViewById(R.id.searchViewUniversity);

        mbase = FirebaseDatabase.getInstance().getReference("University");
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();


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



        recyclerView=findViewById(R.id.recycler_view_university_id);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        list=new ArrayList<>();
        campainAdapter=new UniversityAdapter(this,list,contryName);
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

        RadioGroup top,publics,privates,suggest;
        RadioButton topTrue,topFalse,publicsTrue,publicsFalse,privatesTrue,privatesFalse,suggestTrue,suggestFalse;
        top=view.findViewById(R.id.group_radio_top);
        publics=view.findViewById(R.id.radio_group_public);
        privates=view.findViewById(R.id.radio_group_private);
        suggest=view.findViewById(R.id.radio_group_suggest);

        topTrue=view.findViewById(R.id.top_true_id);
        topFalse=view.findViewById(R.id.top_false_id);
        publicsTrue=view.findViewById(R.id.public_true_id);
        publicsFalse=view.findViewById(R.id.public_false_id);
        privatesTrue=view.findViewById(R.id.private_true_id);
        privatesFalse=view.findViewById(R.id.private_false_id);
        suggestTrue=view.findViewById(R.id.suggest_true_id);
        suggestFalse=view.findViewById(R.id.suggest_false_id);

        post=view.findViewById(R.id.add_university_post_id);

        top.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = view.findViewById(checkedId);
                checkedTextTop = checkedRadioButton.getText().toString();

            }
        });
        publics.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = view.findViewById(checkedId);
                checkedTextPublic = checkedRadioButton.getText().toString();

            }
        });
        privates.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = view.findViewById(checkedId);
                checkedTextPrivates = checkedRadioButton.getText().toString();

            }
        });
        suggest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = view.findViewById(checkedId);
                checkedTextSuggest = checkedRadioButton.getText().toString();

            }
        });


        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(uniName.getText().toString()) && !TextUtils.isEmpty(uniImage.getText().toString()) && !TextUtils.isEmpty(uniLLink.getText().toString())
                && !TextUtils.isEmpty(checkedTextPrivates) && !TextUtils.isEmpty(checkedTextPublic) && !TextUtils.isEmpty(checkedTextSuggest) && !TextUtils.isEmpty(checkedTextTop) ){
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

        String entryKey = FirebaseDatabase.getInstance().getReference("University").push().getKey();
        String s_name=uniName.getText().toString();
        String s_link=uniLLink.getText().toString();
        String s_image=uniImage.getText().toString();

        Map<String,Object> map=new HashMap<>();
        map.put("contryName",contryName);
        map.put("uniWebLink",s_link);
        map.put("uniName",s_name);
        map.put("uniImageLink",s_image);
        map.put("privates",checkedTextPrivates);
        map.put("publics",checkedTextPublic);
        map.put("best",checkedTextTop);
        map.put("suggested",checkedTextSuggest);
        map.put("key",entryKey);
        map.put("date",date);

        // Toast.makeText(UniversityActivity.this, ""+checkedTextTop+" "+checkedTextPublic+" "+checkedTextPrivates+" "+checkedTextSuggest, Toast.LENGTH_SHORT).show();


        FirebaseDatabase.getInstance().getReference("University").child(entryKey).setValue(map).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UniversityActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void searchList(String newText) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<UniversityModel> fList = new ArrayList<>();
                for (UniversityModel obj : list) {
                    if (obj.getUniName().toLowerCase().replaceAll("\\s","").contains(newText.toLowerCase().replaceAll("\\s",""))) {
                        fList.add(obj);
                    }
                }

                // Update the UI on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        campainAdapter.searchListsFunc((ArrayList<UniversityModel>) fList);
                        campainAdapter.notifyDataSetChanged();

                    }
                });
            }
        }).start();
    }


}