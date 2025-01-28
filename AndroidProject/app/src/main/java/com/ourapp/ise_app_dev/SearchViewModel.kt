package com.ourapp.ise_app_dev

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _searchResults = MutableLiveData<List<Project>>()
    val searchResults: LiveData<List<Project>> get() = _searchResults

    fun search(query: String) {
        Log.d("SearchViewModel", "Searching with query: $query")
        RetrofitClient.api.searchProjects(query).enqueue(object : Callback<List<Project>> {
            override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>) {
                if (response.isSuccessful) {
                    _searchResults.postValue(response.body()) // Successfully received the data
                } else {
                    // Handle failure, like showing a toast or logging the error
                    Log.e("SearchViewModel", "Failed to get response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                // Handle error, maybe log the exception or show a Toast message
                Log.e("SearchViewModel", "Request failed: ${t.message}")
            }
        })
    }
}
