package com.sagimymon.heartme.model.network;

import com.sagimymon.heartme.model.BloodTests;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("bloodTestConfig.json")
    Call<BloodTests> getBloodTest();
}
