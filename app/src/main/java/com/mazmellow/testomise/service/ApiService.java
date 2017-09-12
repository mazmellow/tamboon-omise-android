package com.mazmellow.testomise.service;

import com.mazmellow.testomise.model.CharityModel;
import com.mazmellow.testomise.model.RequestModel;
import com.mazmellow.testomise.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers({"Cache-Control: no-cache"})
    @GET("/")
    Call<List<CharityModel>> requestCharityList();

    @Headers({"Cache-Control: no-cache"})
    @POST("/donate")
    Call<ResponseModel> requestCharityDonate(@Body RequestModel model);

}
