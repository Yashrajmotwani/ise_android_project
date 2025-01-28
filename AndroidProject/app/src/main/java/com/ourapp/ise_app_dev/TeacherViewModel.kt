package com.ourapp.ise_app_dev

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherViewModel(application: Application) : AndroidViewModel(application) {

    private val _fsearchResults = MutableLiveData<List<Teacher>>()
    val fsearchResults: LiveData<List<Teacher>> get() = _fsearchResults

    fun fsearch(query: String) {
        Log.d("TeacherViewModel", "Searching with query: $query")
        RetrofitClient.api.searchFaculty(query).enqueue(object : Callback<List<Teacher>> {
            override fun onResponse(call: Call<List<Teacher>>, response: Response<List<Teacher>>) {
                if (response.isSuccessful) {
                    _fsearchResults.postValue(response.body()) // Successfully received the data
                } else {
                    // Handle failure, like showing a toast or logging the error
                    Log.e("SearchViewModel", "Failed to get response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Teacher>>, t: Throwable) {
                // Handle error, maybe log the exception or show a Toast message
                Log.e("TeacherViewModel", "Request failed: ${t.message}")
            }
        })
    }
}
