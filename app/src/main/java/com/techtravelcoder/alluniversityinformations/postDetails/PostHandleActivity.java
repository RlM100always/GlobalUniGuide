package com.techtravelcoder.alluniversityinformations.postDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.techtravelcoder.alluniversityinformation.R;

public class PostHandleActivity extends AppCompatActivity {

    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_handle);
        int color = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.back);
        }
        getWindow().setStatusBarColor(color);

        ViewPager2 viewPager = findViewById(R.id.vpager);
        PageAdapter pageAdapter = new PageAdapter(this);
        viewPager.setAdapter(pageAdapter);

         tabLayout = findViewById(R.id.tablayout1);


        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Set tab titles as needed based on position
            switch (position) {
                case 0:
                    tab.setText("Home");
                    break;
                case 1:
                    tab.setText("Category");
                    break;
                case 2:
                    tab.setText("Recent");
                    break;
                case 3:
                    tab.setText("Favorite");
                    break;
            }
        }).attach();


    }


}