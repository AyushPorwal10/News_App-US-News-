package com.example.api;

import android.content.Context;
import android.widget.Toast;

import java.security.cert.Extension;

import Models.NewsApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;

    public static final String URL = "https://newsapi.org/v2/";


    Retrofit retrofit   = new Retrofit.Builder()
            .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
            .build();
    public RequestManager(Context context) {
        this.context = context;
    }

    public void getNewsHeadlines(OnFetchDataListener listener , String category , String query ){
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
        Call<NewsApiResponse> call = callNewsApi.callHeadlines("in",category,query ,context.getString(R.string.api_key));
        try{
            call.enqueue(new Callback<NewsApiResponse>() {
                @Override
                public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(context,"Error in Response",Toast.LENGTH_LONG).show();
                    }
                    else{
                        listener.onFetchData(response.body().getArticles());
                    }
                }

                @Override
                public void onFailure(Call<NewsApiResponse> call, Throwable throwable) {
                    listener.onError("Request Failed");
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public interface CallNewsApi {
        @GET("top-headlines")
        Call<NewsApiResponse> callHeadlines (
            @Query("country") String country ,
            @Query("category") String category ,
            @Query("q") String query ,
            @Query("apiKey")String api_key
        );
    }
}
