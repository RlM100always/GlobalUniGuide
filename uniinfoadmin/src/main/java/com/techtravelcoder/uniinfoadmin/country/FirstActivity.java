package com.techtravelcoder.uniinfoadmin.country;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.techtravelcoder.uniinfoadmin.R;
import com.techtravelcoder.uniinfoadmin.post.PostDetailsActivity;

public class FirstActivity extends AppCompatActivity {

    TextView country,post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        country=findViewById(R.id.country_id);
        post=findViewById(R.id.post_details_id);

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), PostDetailsActivity.class);
                startActivity(intent);

            }
        });
    }
}