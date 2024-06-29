package com.techtravelcoder.uniinfoadmin.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.List;
import java.util.Map;

public class PostDetailsActivity extends AppCompatActivity {



    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private EditText title,label,link,id;
    private CategoryModel categoryModel;
    private CategoryAdapter categoryAdapter;
    private ArrayList<CategoryModel>list;
    private DatabaseReference mbase1;
    private RadioGroup radioGroup;
    private RadioButton lesson,random;
    private Boolean check;
    private androidx.appcompat.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        recyclerView=findViewById(R.id.post_details_recycler_view_id);
        floatingActionButton=findViewById(R.id.post_details_float_button);


        mbase1 = FirebaseDatabase.getInstance().getReference("Category");

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        searchView=findViewById(R.id.searchViewPostDetails);

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
        categoryAdapter=new CategoryAdapter(list,PostDetailsActivity.this);
        recyclerView.setAdapter(categoryAdapter );

        mbase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        categoryModel = dataSnapshot.getValue(CategoryModel.class);
                        //Toast.makeText(PostDetailsActivity.this, ""+categoryModel.getLabel(), Toast.LENGTH_SHORT).show();

                        if(categoryModel != null){
                            list.add(0,categoryModel);
                        }

                    }

                }
                categoryAdapter.notifyDataSetChanged();

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
        AlertDialog.Builder builder=new AlertDialog.Builder(PostDetailsActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.post_category_design,null);

        title=view.findViewById(R.id.category_title_id);
        label=view.findViewById(R.id.categoty_label_id);
        link=view.findViewById(R.id.category_image_link_id);
        id=view.findViewById(R.id.unique_id);
        radioGroup=view.findViewById(R.id.group_radio_category);
        lesson=view.findViewById(R.id.lesson_id);
        random=view.findViewById(R.id.random_id);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (checkedId == R.id.lesson_id) {
                   check=true;

                } else if (checkedId == R.id.random_id) {
                    check=false;
                }
            }
        });

        TextView posttv=view.findViewById(R.id.add_news_category_id);


        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();
        alertDialog.setCancelable(false);

        posttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostDetailsActivity.this, ""+check, Toast.LENGTH_SHORT).show();
                if(!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(link.getText().toString()) && !TextUtils.isEmpty(label.getText().toString())
                && !TextUtils.isEmpty(id.getText().toString()) && check!=null){
                    uploadData();
                    alertDialog.dismiss();

                }

            }
        });


    }

    private void uploadData() {

        String entryKey = FirebaseDatabase.getInstance().getReference("Category").push().getKey();
        String s_title=title.getText().toString();
        String s_link=link.getText().toString();
        String s_label=label.getText().toString();

        Map<String,Object> map=new HashMap<>();
        map.put("name",s_title);
        map.put("imageLink",s_link);
        map.put("label",s_label);
        map.put("key",entryKey);
        map.put("id",id.getText().toString());
        map.put("categoryType",check);


        //Toast.makeText(this, ""+s_title+" "+s_label+" "+entryKey+" "+s_link, Toast.LENGTH_SHORT).show();


        FirebaseDatabase.getInstance().getReference("Category").child(entryKey).setValue(map).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PostDetailsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void searchList(String newText) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<CategoryModel> fList = new ArrayList<>();
                for (CategoryModel obj : list) {
                    if (obj.getName().toLowerCase().trim().replaceAll("\\s","").contains(newText.toLowerCase().trim().replaceAll("\\s",""))) {
                        fList.add(obj);
                    }
                }

                // Update the UI on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        categoryAdapter.searchListsFunc((ArrayList<CategoryModel>) fList);
                        categoryAdapter.notifyDataSetChanged();

                    }
                });
            }
        }).start();
    }


}