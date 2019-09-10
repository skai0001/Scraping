package com.algonquincollege.skai0001.retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    HashMap<String, Integer> occurrences = new HashMap<>();
    WordsAdapter wordsAdapter;
    final static String BASE_URL = "https://cheetahnetworks.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.wordList);


        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient).build();


        final ApiService apiService = retrofit.create(ApiService.class);


        Call<String> stringCall = apiService.getStringResponse();


        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    String responseString = response.body();
                    Document doc = Jsoup.parse(responseString);
                    responseString = doc.text();
                    createHashMap(responseString);


                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    private void createHashMap(String responseString) {


        responseString = responseString.replaceAll("[^a-zA-Z0-9]", " "); //replace non alphanumerics with empty string

        String[] splitWords = responseString.split(" +");

        for (String word : splitWords) {

            if (StringUtil.isNumeric(word)) {
                continue;
            }

            Integer oldCount = occurrences.get(word);
            if (oldCount == null) {
                oldCount = 0;
            }
            occurrences.put(word, oldCount + 1);

            occurrences.get(word);


        }


        wordsAdapter = new WordsAdapter(this, occurrences);
        recyclerView.setAdapter(wordsAdapter);
    }


}