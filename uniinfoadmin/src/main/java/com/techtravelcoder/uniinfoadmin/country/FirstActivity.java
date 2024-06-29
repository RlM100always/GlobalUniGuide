package com.techtravelcoder.uniinfoadmin.country;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.techtravelcoder.uniinfoadmin.R;
import com.techtravelcoder.uniinfoadmin.adsNotifi.AdsNotifiActivity;
import com.techtravelcoder.uniinfoadmin.book.BookCategoryActivity;
import com.techtravelcoder.uniinfoadmin.post.PostDetailsActivity;

public class FirstActivity extends AppCompatActivity {

    TextView country,post,book,adsNotifiTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        country=findViewById(R.id.country_id);
        post=findViewById(R.id.post_details_id);
        book=findViewById(R.id.book_id);
        adsNotifiTv=findViewById(R.id.ads_notification_id);


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
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), BookCategoryActivity.class);
                startActivity(intent);

            }
        });

        adsNotifiTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), AdsNotifiActivity.class);
                startActivity(intent);
            }
        });

    }
}