package com.mazmellow.testomise.service;

import com.mazmellow.testomise.model.Charity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/")
    Call<List<Charity>> requestCharityList();

}
