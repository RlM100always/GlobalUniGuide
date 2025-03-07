package com.techtravelcoder.uniinfoadmin.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

public class BookPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private SearchView searchView;
    private String bCataKey,bCataNAme;

    private ArrayList<BookPostModel> bookList;
    private BookPostAdapter bookPostAdapter;
    BookPostModel bookPostModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_post);

        bCataKey=getIntent().getStringExtra("key");
        bCataNAme=getIntent().getStringExtra("bCategory");



        //initialize
        searchView=findViewById(R.id.book_post_search);
        recyclerView=findViewById(R.id.book_post_recyclerview_id);
        floatingActionButton=findViewById(R.id.book_post_float_id);

        retriveBookDetailsData();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputBookFromAdmin();
            }
        });
    }

    private void retriveBookDetailsData() {
        bookList=new ArrayList<>();
        bookPostAdapter=new BookPostAdapter(BookPostActivity.this,bookList,bCataNAme);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        recyclerView.setAdapter(bookPostAdapter);
        FirebaseDatabase.getInstance().getReference("Book Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        bookPostModel = dataSnapshot.getValue(BookPostModel.class);
                        if(bookPostModel != null && bookPostModel.getBookCategoryKey().equals(bCataKey)){
                            bookList.add(0,bookPostModel);

                        }

                    }

                }
                bookPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void inputBookFromAdmin() {
        AlertDialog.Builder builder=new AlertDialog.Builder(BookPostActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.book_post_design,null);

        EditText bookName=view.findViewById(R.id.ed_book_post_name_id);
        EditText bookImageLink=view.findViewById(R.id.ed_book_post_image_link_id);
        EditText bookDriveUrl=view.findViewById(R.id.ed_book_post_drive_url_id);


        TextView addBook=view.findViewById(R.id.book_post_add_id);


        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(bookName.getText().toString()) && !TextUtils.isEmpty(bookImageLink.getText().toString()) && !TextUtils.isEmpty(bookDriveUrl.getText().toString())){

                    uploadBooKDetailsInFirebase(bookName,bookImageLink,bookDriveUrl);

                    alertDialog.dismiss();

                }

            }
        });
    }

    private void uploadBooKDetailsInFirebase(EditText bookName, EditText bookImageLink, EditText bookDriveUrl) {
        String entryKey = FirebaseDatabase.getInstance().getReference("Book Details").push().getKey();

        Calendar calendar = Calendar.getInstance();
        Date times=calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
        String date = sdf.format(times);

        Map<String,Object> map=new HashMap<>();
        map.put("bookName",bookName.getText().toString());
        map.put("bookImageLink",bookImageLink.getText().toString());
        map.put("bookKey",entryKey);
        map.put("bookDriveurl",bookDriveUrl.getText().toString());
        map.put("bookCategoryKey",bCataKey);
        map.put("bookPostDate",date);
        map.put("bookCategoryName",bCataNAme);

        Toast.makeText(this, ""+bookName.getText().toString()+" "+bookDriveUrl.getText().toString()+" "+entryKey, Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference("Book Details").child(entryKey).setValue(map).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(BookPostActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}