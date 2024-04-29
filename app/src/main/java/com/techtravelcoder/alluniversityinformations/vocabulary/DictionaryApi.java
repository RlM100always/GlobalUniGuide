package com.techtravelcoder.alluniversityinformations.vocabulary;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface DictionaryApi {

    @GET("en/{word}")
    Call<List<WordResult>> getMeaning(@Path("word") String word);

}