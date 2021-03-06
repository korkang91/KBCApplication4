package com.kangbc.kbcapplication4;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mac on 2017. 6. 22..
 */

public interface ApiInterface {

    //@GET("/data/2.5/weather?lat={lat}&lon={lon}&appid=684b98e21b4f35b7d52abe9ff6279349")
    //Call<Repo> repo(@Path("lat") String lat, @Path("lon") String lon);
    @GET("/data/2.5/weather")
    Call<Repo> repo(@Query("appid") String appid, @Query("lat") double lat, @Query("lon") double lon);
}