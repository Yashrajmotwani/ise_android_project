package com.ourapp.ise_app_dev

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ApiService {

    // This will define the 'GET /search' endpoint that expects a query parameter
    @GET("search")
    fun searchProjects(
        @Query("query") query: String // The 'query' parameter to search for projects
    ): Call<List<Project>> // Return a list of 'Project' objects from the response
}