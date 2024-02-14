package com.techtravelcoder.alluniversityinformation.universityDetails;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformation.countryDetails.CountryModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniversityActivity extends AppCompatActivity {


    RecyclerView recyclerView,recyclerView1,recyclerViewPub,recyclerViewPrv;
    TextView cName,bestUni,allUni,pubUni,prvUni;
    SwipeRefreshLayout swipeRefreshLayout;

    private UniversityAdapter campainAdapter;
    private UniversityAdapter1 campainAdapter1;
    private UniversityAdapterPub universityAdapterPub;
    private UniversityAdapterPrv universityAdapterPrv;


    private ArrayList<UniversityModel> list,list1,listPub,listPrv;
    UniversityModel campainModel,campainModel1,campainModelPub,campainModelPrv;
    DatabaseReference mbase,mbase1,mbasePub,mbasePrv;
    SearchView searchView;

    String contryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);

        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.back);
        }
        getWindow().setStatusBarColor(color);

        cName=findViewById(R.id.uni_coununtry_name_id);
        searchView=findViewById(R.id.searchView_uni);
        bestUni=findViewById(R.id.best_uni_id);
        allUni=findViewById(R.id.all_uni_id);
        pubUni=findViewById(R.id.public_uni_id);
        prvUni=findViewById(R.id.private_uni_id);
        swipeRefreshLayout=findViewById(R.id.university_suffle_id);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                // After refreshing, call setRefreshing(false) to stop the loading animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        contryName=getIntent().getStringExtra("name");
        cName.setText(""+contryName+" University Guidelines");
        bestUni.setText("Top Ranking "+contryName+" University");
        allUni.setText("All "+contryName+" University");
        pubUni.setText(contryName+" Public University");
        prvUni.setText(contryName+" Private University");

        mbase = FirebaseDatabase.getInstance().getReference("University");
        mbase1 = FirebaseDatabase.getInstance().getReference("University");
        mbasePub=FirebaseDatabase.getInstance().getReference("University");
        mbasePrv=FirebaseDatabase.getInstance().getReference("University");



        allUniversity();
        topUniversity();
        publicUniversity();
        privateUniversity();


    }

    private void allUniversity(){
        recyclerView=findViewById(R.id.recycler_view_university_id);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
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

                        if(campainModel != null  &&campainModel.getContryName() != null&& campainModel.getContryName().equals(contryName)){
                            list.add(0,campainModel);
                        }

                    }

                }
                campainAdapter.notifyDataSetChanged();
                Collections.shuffle(list);
                campainAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchList(String newText) {
        List<UniversityModel> fListCampain = new ArrayList<>();
        List<UniversityModel> fListCampain1 = new ArrayList<>();
        List<UniversityModel> fListPub = new ArrayList<>();
        List<UniversityModel> fListPrv = new ArrayList<>();

        for (UniversityModel obj : list) {
            if (obj.getUniName().toLowerCase().contains(newText.toLowerCase())) {
                fListCampain.add(obj);
            }
        }
        campainAdapter.searchList((ArrayList<UniversityModel>) fListCampain);

        for (UniversityModel obj : list1) {
            if (obj.getUniName().toLowerCase().contains(newText.toLowerCase())) {
                fListCampain1.add(obj);
            }
        }
        campainAdapter1.searchList1((ArrayList<UniversityModel>) fListCampain1);

        for (UniversityModel obj : listPub) {
            if (obj.getUniName().toLowerCase().contains(newText.toLowerCase())) {
                fListPub.add(obj);
            }
        }
        universityAdapterPub.searchListPub((ArrayList<UniversityModel>) fListPub);

        for (UniversityModel obj : listPrv) {
            if (obj.getUniName().toLowerCase().contains(newText.toLowerCase())) {
                fListPrv.add(obj);
            }
        }
        universityAdapterPrv.searchListPrv((ArrayList<UniversityModel>) fListPrv);
    }

    private void privateUniversity() {
        recyclerViewPrv=findViewById(R.id.private_recyclerview_id);
        recyclerViewPrv.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.HORIZONTAL,false));
        listPrv=new ArrayList<>();
        universityAdapterPrv=new UniversityAdapterPrv(this,listPrv);
        recyclerViewPrv.setAdapter(universityAdapterPrv);

        mbasePrv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listPrv.clear();

                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        campainModelPrv = dataSnapshot.getValue(UniversityModel.class);
                        //&& campainModel.getCountryName().equals(contryName)


                        if(campainModelPrv != null  &&campainModelPrv.getContryName() != null&& campainModelPrv.getContryName().equals(contryName)
                                && campainModelPrv.getPrivates().equals("private")){
                            listPrv.add(0,campainModelPrv);

                        }

                    }

                }
                universityAdapterPrv.notifyDataSetChanged();
                Collections.shuffle(listPrv);
                campainAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void topUniversity() {
        recyclerView1=findViewById(R.id.top_recyclerview_id);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        list1=new ArrayList<>();
        campainAdapter1=new UniversityAdapter1(this,list1);
        recyclerView1.setAdapter(campainAdapter1);

        mbase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list1.clear();

                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        campainModel1 = dataSnapshot.getValue(UniversityModel.class);
                        //&& campainModel.getCountryName().equals(contryName)


                        if(campainModel1 != null  && campainModel1.getContryName() != null && campainModel1.getContryName().equals(contryName)
                        && campainModel1.getBest().equals("top")){
                            list1.add(0,campainModel1);

                        }

                    }

                }
                campainAdapter1.notifyDataSetChanged();
                Collections.shuffle(list1);
                campainAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void publicUniversity(){
        recyclerViewPub=findViewById(R.id.public_recyclerview_id);
        recyclerViewPub.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.HORIZONTAL,false));
        listPub=new ArrayList<>();
        universityAdapterPub=new UniversityAdapterPub(this,listPub);
        recyclerViewPub.setAdapter(universityAdapterPub);

        mbasePub.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listPub.clear();

                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        campainModelPub = dataSnapshot.getValue(UniversityModel.class);
                        //&& campainModel.getCountryName().equals(contryName)


                        if(campainModelPub != null  &&campainModelPub.getContryName() != null&& campainModelPub.getContryName().equals(contryName)
                        && campainModelPub.getPublics().equals("public")){
                            listPub.add(0,campainModelPub);

                        }

                    }

                }
                universityAdapterPub.notifyDataSetChanged();
                Collections.shuffle(listPub);
                campainAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void refreshData() {
        allUniversity();
        privateUniversity();
        publicUniversity();
        topUniversity();
        swipeRefreshLayout.setRefreshing(false);
    }



}