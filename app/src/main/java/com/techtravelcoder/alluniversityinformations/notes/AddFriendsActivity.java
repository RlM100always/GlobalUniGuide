package com.techtravelcoder.alluniversityinformations.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.books.BookCategoryActivity;
import com.techtravelcoder.alluniversityinformations.books.BookCategoryAdapter;
import com.techtravelcoder.alluniversityinformations.books.BookCategoryModel;
import com.techtravelcoder.alluniversityinformations.books.BookPostModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddFriendsActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private String check;
    private RecyclerView recyclerView;
    private ArrayList<NotesModel> noteList;
    private ArrayList<FriendsModel> flist;

    private NotesAdapter notesAdapter;
    private FriendsAdapter friendsAdapter;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        floatingActionButton=findViewById(R.id.friends_float_id);
        recyclerView=findViewById(R.id.add_friends_recyclerview_id);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh_layout_add_friends);
        searchView=findViewById(R.id.friends_search);
        check=getIntent().getStringExtra("check");

        if(check.equals("2")){
            retriveNotesData();
        }
        if(check.equals("1")){
            retriveFriendsData();
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(check.equals("2")){
                    retriveNotesData();
                    swipeRefreshLayout.setRefreshing(false);
                }
                if(check.equals("1")){
                    retriveFriendsData();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(check.equals("1")){
                    searchListFriends(newText);
                }
                if(check.equals("2")){
                    searchListNotes(newText);
                }

                return true;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.equals("1")){
                    addFriends();
                }
                if(check.equals("2")){
                    addNotes();
                }
            }
        });


    }

    private void searchListFriends(String newText) {
        List<FriendsModel> filteredList1 = new ArrayList<>();
        String queryWithoutSpaces = newText.replaceAll("\\s+", "").toLowerCase(); // Remove spaces from query

        for (FriendsModel obj : flist) {
            String objStringWithoutSpaces = obj.toString().replaceAll("\\s+", "").toLowerCase(); // Remove spaces from object

            // Perform search based on bCategoryName without spaces and case-insensitive
            if (objStringWithoutSpaces.contains(queryWithoutSpaces)) {
                filteredList1.add(obj);
            }
        }

        // Update your UI with the filtered list
        friendsAdapter.searchLists((ArrayList<FriendsModel>) filteredList1);
        friendsAdapter.notifyDataSetChanged();
    }

    private void retriveFriendsData() {

        flist=new ArrayList<>();
        friendsAdapter=new FriendsAdapter( AddFriendsActivity.this,flist);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView.setAdapter(friendsAdapter);
        FirebaseDatabase.getInstance().getReference("Friends").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                flist.clear();
                FriendsModel friendsModel;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        friendsModel = dataSnapshot.getValue(FriendsModel.class);
                        if(friendsModel != null){
                            flist.add(0,friendsModel);

                        }

                    }
                }

                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void searchListNotes(String query) {
        List<NotesModel> filteredList = new ArrayList<>();
        String queryWithoutSpaces = query.replaceAll("\\s+", "").toLowerCase(); // Remove spaces from query

        for (NotesModel obj : noteList) {
            String objStringWithoutSpaces = obj.toString().replaceAll("\\s+", "").toLowerCase(); // Remove spaces from object

            // Perform search based on bCategoryName without spaces and case-insensitive
            if (objStringWithoutSpaces.contains(queryWithoutSpaces)) {
                filteredList.add(obj);
            }
        }

        // Update your UI with the filtered list
        notesAdapter.searchLists((ArrayList<NotesModel>) filteredList);
        notesAdapter.notifyDataSetChanged();
    }


    private void retriveNotesData() {
         noteList=new ArrayList<>();
         notesAdapter=new NotesAdapter(noteList, AddFriendsActivity.this);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView.setAdapter(notesAdapter);
        FirebaseDatabase.getInstance().getReference("MyNotes").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteList.clear();
                NotesModel notesModel;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        notesModel = dataSnapshot.getValue(NotesModel.class);
                        if(notesModel != null){
                            noteList.add(0,notesModel);

                        }

                    }
                }

                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addNotes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddFriendsActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.notes_design, null);
        builder.setView(view);

        final TextInputEditText title = view.findViewById(R.id.notes_title_id);
        final TextInputEditText writeNotes = view.findViewById(R.id.notes_write_id);
        TextView saveButton = view.findViewById(R.id.notes_save_id);
        TextView cancelButton = view.findViewById(R.id.notes_cancel_id);

        final AlertDialog alertDialog = builder.create();
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.note_back);
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(drawable);
        }
        alertDialog.show();
        alertDialog.setCancelable(false);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(writeNotes.getText().toString())) {
                    String key = FirebaseDatabase.getInstance().getReference("MyNotes").push().getKey();
                    Calendar calendar = Calendar.getInstance();
                    Date times = calendar.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, EEEE", Locale.getDefault());
                    String date = sdf.format(times);

                    Map<String, Object> map = new HashMap<>();
                    map.put("title", title.getText().toString());
                    map.put("notes", writeNotes.getText().toString());
                    map.put("date", date);
                    map.put("key",key);


                    FirebaseDatabase.getInstance().getReference("MyNotes")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child(key)
                            .setValue(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AddFriendsActivity.this, "Successfully Add Note", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddFriendsActivity.this, "Failed to save note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(AddFriendsActivity.this, "Notes is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void addFriends() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddFriendsActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.add_friends_design, null);

        // Initialize UI components from the dialog layout
        final TextInputEditText nameEditText = view.findViewById(R.id.f_name_id);
        final TextInputEditText phoneEditText = view.findViewById(R.id.f_phone_no_id);
        final TextInputEditText addressEditText = view.findViewById(R.id.f_address_id);
        final TextInputEditText institutionEditText = view.findViewById(R.id.f_institute_id);
        final TextInputEditText departmentEditText = view.findViewById(R.id.f_department_id);
        final TextInputEditText facebookEditText = view.findViewById(R.id.f_facebook_id);
        final TextInputEditText notesEditText = view.findViewById(R.id.f_Notes_id);
        TextView saveButton = view.findViewById(R.id.f_save_id);
        TextView cancelButton = view.findViewById(R.id.f_cancel_id);

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.note_back);
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(drawable);
        }
        alertDialog.show();
        alertDialog.setCancelable(false);

        // Set click listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String institution = institutionEditText.getText().toString().trim();
                String department = departmentEditText.getText().toString().trim();
                String facebook = facebookEditText.getText().toString().trim();
                String notes = notesEditText.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
                    Toast.makeText(AddFriendsActivity.this, "Name and Phone Number are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Generate a unique key for each entry
                String key = FirebaseDatabase.getInstance().getReference("Friends").push().getKey();


                Calendar calendar = Calendar.getInstance();
                Date times=calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
                String date = sdf.format(times);

                // Create a map to store data
                Map<String, Object> friendData = new HashMap<>();
                friendData.put("name", name);
                friendData.put("phone", phone);
                friendData.put("address", address);
                friendData.put("institution", institution);
                friendData.put("department", department);
                friendData.put("facebook", facebook);
                friendData.put("notes", notes);
                friendData.put("date",date);
                friendData.put("key",key);



                // Save data to Firebase
                FirebaseDatabase.getInstance().getReference("Friends").child(FirebaseAuth.getInstance().getUid()).child(key).setValue(friendData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddFriendsActivity.this, "Successfully Add Friends Information", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddFriendsActivity.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Set click listener for the cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}