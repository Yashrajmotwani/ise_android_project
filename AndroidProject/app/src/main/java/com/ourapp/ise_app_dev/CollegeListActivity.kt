package com.ourapp.ise_app_dev

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollegeListActivity : AppCompatActivity() {

    private lateinit var collegeAdapter: CollegeAdapter
    private val collegeList: MutableList<College> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_college_list)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        collegeAdapter = CollegeAdapter(collegeList)
        recyclerView.adapter = collegeAdapter

        colleges()
    }

    // Fetch colleges from the server using Retrofit
    private fun colleges() {
        // Using the Retrofit client to make the request
        RetrofitClient.api.getColleges().enqueue(object : Callback<List<College>> {
            override fun onResponse(call: Call<List<College>>, response: Response<List<College>>) {
                if (response.isSuccessful) {
                    // Successfully got the response, update the list
                    response.body()?.let {
                        collegeList.clear()
                        collegeList.addAll(it)
                        collegeAdapter.notifyDataSetChanged()  // Notify the adapter to refresh
                    }
                } else {
                    // Handle the error response
                    Toast.makeText(applicationContext, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<College>>, t: Throwable) {
                // Handle failure scenario
                Toast.makeText(applicationContext, "Failed to fetch data: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
