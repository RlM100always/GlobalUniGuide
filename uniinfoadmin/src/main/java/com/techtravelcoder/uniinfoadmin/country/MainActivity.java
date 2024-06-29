package com.techtravelcoder.uniinfoadmin.country;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText title,link;
    private CountryAdapter campainAdapter;
    private ArrayList<CountryModel> list;
    DatabaseReference mbase;
    FloatingActionButton floatingActionButton;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView=findViewById(R.id.recycler_view_id);

        floatingActionButton=findViewById(R.id.withdraw_set_float_button);
        searchView=findViewById(R.id.searchView);

        mbase = FirebaseDatabase.getInstance().getReference("Country");

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


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        list=new ArrayList<>();
        campainAdapter=new CountryAdapter(this,list);
        recyclerView.setAdapter(campainAdapter );

        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                CountryModel campainModel;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        campainModel = dataSnapshot.getValue(CountryModel.class);
                        if(campainModel != null){
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


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDialogue();
            }
        });
    }

    private void postDialogue() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.task_input_design,null);
        title=view.findViewById(R.id.enter_task_title);
        link=view.findViewById(R.id.enter_task_link);
        TextView posttv=view.findViewById(R.id.add_post_id);


        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();
        posttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(link.getText().toString())){
                    uploadData();
                    alertDialog.dismiss();

                }

            }
        });


    }

    private void uploadData() {

        String entryKey = FirebaseDatabase.getInstance().getReference("University").push().getKey();
        String s_title=title.getText().toString();
        String s_link=link.getText().toString();
        Map<String,Object> map=new HashMap<>();
        map.put("name",s_title);
        map.put("imageLink",s_link);
        map.put("key",entryKey);



        FirebaseDatabase.getInstance().getReference("Country").child(entryKey).setValue(map).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void searchList(String newText) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<CountryModel> fList = new ArrayList<>();
                for (CountryModel obj : list) {
                    if (obj.getName().toLowerCase().replaceAll("\\s","").contains(newText.toLowerCase().replaceAll("\\s",""))) {
                        fList.add(obj);
                    }
                }

                // Update the UI on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        campainAdapter.searchListsFunc((ArrayList<CountryModel>) fList);
                        campainAdapter.notifyDataSetChanged();

                    }
                });
            }
        }).start();
    }



}