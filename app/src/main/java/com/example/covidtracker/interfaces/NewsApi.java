package com.example.covidtracker.interfaces;

import com.example.covidtracker.models.dataModels.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    String NEWS_API_KEY = "900b5cbb66ec4912bf2a6107e3dab9ac";
    int NO_OF_ARTICLES = 100;
    String QUERY_KEYWORD = "corona OR coronavirus OR covid-19 OR covid AND (India OR Kerala OR Haryana OR Maharashtra OR Uttar Pradesh OR Delhi OR Karnataka OR Jammu and Kashmir OR Rajasthan OR Andhra Pradesh OR Punjab OR Tamil Nadu OR Telangana OR Assam OR Bihar OR Gujarat OR Meghalaya OR Madhya Pradesh OR Odisha OR Puducherry OR Tripura OR West Bengal OR Goa OR Arunachal Pradesh OR Chhattisgarh OR Himachal Pradesh OR Jharkhand OR Manipur OR Mizoram OR Nagaland OR Sikkim OR Uttarakhand OR Chandigarh OR Dadra and Nagar Haveli OR Daman and Diu OR Lakshadweep)";
    String SORT_BY = "publishedAt";
    String LANGUAGE = "en";
    String COUNTRY = "in";

    @GET("v2/everything")
    Call<NewsResponse> getNews(
            @Query(value = "q", encoded = true) String query,
            @Query("apiKey") String apiKey,
            @Query("pageSize") int pageSize,
            @Query("sortBy") String sort,
            @Query("language") String language);

}
