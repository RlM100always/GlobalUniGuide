package com.techtravelcoder.alluniversityinformations.vocabulary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.postDetails.PostWebViewActivity;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocabularyActivity extends AppCompatActivity {


    private EditText searchInput;
    private Button searchBtn;
    private TextView wordTextview;
    private TextView phoneticTextview;
    private RecyclerView meaningRecyclerView;
    private ProgressBar progressBar;
    private ImageView vocabu;

    private MeaningAdapter adapter;
    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        searchInput = findViewById(R.id.search_input);
        searchBtn = findViewById(R.id.search_btn);
        wordTextview = findViewById(R.id.word_textview);
        phoneticTextview = findViewById(R.id.phonetic_textview);
        meaningRecyclerView = findViewById(R.id.meaning_recycler_view);
        progressBar = findViewById(R.id.progress_bar12);
        vocabu=findViewById(R.id.voca_image_image);

        int color=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.whiteTextSideColor1);
        }
        getWindow().setStatusBarColor(color);





        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = searchInput.getText().toString();
                getMeaning(word);
            }
        });

        adapter = new MeaningAdapter(Collections.emptyList(),VocabularyActivity.this);
        meaningRecyclerView.setLayoutManager(new LinearLayoutManager(VocabularyActivity.this));
        meaningRecyclerView.setAdapter(adapter);

    }

    private void getMeaning(String word) {
        setInProgress(true);

        RetrofitInstance.getDictionaryApi().getMeaning(word).enqueue(new Callback<List<WordResult>>() {
            @Override
            public void onResponse(Call<List<WordResult>> call, Response<List<WordResult>> response) {
                setInProgress(false);
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    WordResult firstResult = response.body().get(0);
                    setUI(firstResult);
                } else {
                    vocabu.setVisibility(View.GONE);
                    Toast.makeText(VocabularyActivity.this, "No meaning found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<WordResult>> call, Throwable t) {
                vocabu.setVisibility(View.VISIBLE);
                setInProgress(false);
                Toast.makeText(VocabularyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUI(WordResult response) {
        vocabu.setVisibility(View.GONE);
        wordTextview.setText(response.getWord());

        phoneticTextview.setText(response.getPhonetic());
        adapter.updateNewData(response.getMeanings());


    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            searchBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            searchBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}