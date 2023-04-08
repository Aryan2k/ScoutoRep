package com.example.scouto.network.api;

import com.example.scouto.network.response.manage_car.MakeDetailsResponseData;
import com.example.scouto.network.response.manage_car.ModelDetailsResponseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CarApi {

    @GET("getallmakes?format=json")
    Call<MakeDetailsResponseData> getMakeDetailsResponseData();

    @GET("GetModelsForMakeId/{make_id}?format=json")
    Call<ModelDetailsResponseData> getModelDetailsResponseData(@Path("make_id") long makeId);
}
