package com.algonquincollege.skai0001.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {


    @GET(".")
    Call<String> getStringResponse();
}