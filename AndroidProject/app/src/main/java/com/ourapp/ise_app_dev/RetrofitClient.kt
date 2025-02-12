package com.ourapp.ise_app_dev

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    //    private const val BASE_URL = "http://localhost:5000/" // Change to your server's URL if needed
//     val BASE_URL = "http://10.0.2.2:5000/"
    val BASE_URL = "http://10.25.82.176:5000/"

    // Create a Retrofit instance
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Define the base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java) // Create the ApiService
    }

}
