package com.ourapp.ise_app_dev

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.DELETE


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

    // Save a project as a favorite
    @POST("saveProject/{userId}")
    fun saveFavorite(
        @Path("userId") userId: String, // User's ID from Firebase
        @Body project: Project // Project to be saved as a favorite
    ): Call<Void> // Void response indicating success or failure

    // Remove a project from favorites
    @DELETE("removeProject/{userId}/{projectId}")
    fun removeFavorite(
        @Path("userId") userId: String, // User's ID from Firebase
        @Path("projectId") projectId: String // ID of the project to be removed
    ): Call<Void> // Void response indicating success or failure

    @GET("getFavoriteProjects/{userId}")
    fun getFavoriteProjects(
        @Path("userId") userId: String
    ): Call<List<Project>>

}