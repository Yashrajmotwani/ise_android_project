package com.ourapp.ise_app_dev

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // This will define the 'GET /search' endpoint that expects a query parameter
    @GET("search")
    fun searchProjects(
        @Query("query") query: String // The 'query' parameter to search for projects
    ): Call<List<Project>> // Return a list of 'Project' objects from the response

    @GET("fsearch")
    fun searchFaculty(
        @Query("query") query: String // The 'query' parameter to search for faculty
    ): Call<List<Teacher>>

}