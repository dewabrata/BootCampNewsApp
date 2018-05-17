package com.bootcamp.bootcampcrud.APIService;

/**
 * Created by user on 1/10/2018.
 */



import com.bootcamp.bootcampcrud.servicemodel.newsmodel.NewsModel;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by anupamchugh on 09/01/17.
 */

 public interface APIInterfacesRest {

 @GET("api/bootcampnews/all")
 Call<NewsModel> getNews();


}

