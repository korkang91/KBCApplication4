package com.kangbc.kbcapplication4;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
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
                              ,@Header("X-Naver-Client-Secret") String secret
                              ,@Part MultipartBody.Part file);

    @Multipart
    @POST("/v1/vision/face")
    Call<NaverRepo> naverRepo2(@Header("X-Naver-Client-Id") String id
                              ,@Header("X-Naver-Client-Secret") String secret
                              ,@Part MultipartBody.Part file);
}
