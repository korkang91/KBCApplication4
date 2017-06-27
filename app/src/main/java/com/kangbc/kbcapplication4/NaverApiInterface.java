package com.kangbc.kbcapplication4;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by mac on 2017. 6. 26..
 */

public interface NaverApiInterface {
    @Multipart
    @POST("/v1/vision/celebrity")
    Call<NaverRepo> naverRepo(@Header("X-Naver-Client-Id") String id
                            , @Header("X-Naver-Client-Secret") String secret
                            , @Part("images") RequestBody file);

    @Multipart
    @POST("/v1/vision/celebrity")
    Call<NaverRepo> naverRepo2(@Header("X-Naver-Client-Id") String id
                             , @Header("X-Naver-Client-Secret") String secret
                             , @Part("name=\"image\";") RequestBody file); //form-data; name="image"; filename="t1.jpg"
    @Multipart
    @POST("/v1/vision/celebrity")
    Call<NaverRepo> naverRepo3(@Header("X-Naver-Client-Id") String id
                              ,@Header("X-Naver-Client-Secret") String secret
                              ,@Part("images") RequestBody file);


    @Multipart
    @POST("/v1/vision/celebrity")
    Call<NaverRepo> naverRepo4(@Header("X-Naver-Client-Id") String id
                              ,@Header("X-Naver-Client-Secret") String secret
                              ,@Part MultipartBody.Part file);


}
