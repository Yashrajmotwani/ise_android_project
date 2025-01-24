package com.ourapp.ise_app_dev

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CollegeListActivity : AppCompatActivity() {

    private lateinit var collegeAdapter: CollegeAdapter
    private val collegeList = listOf(
        College("IIT Bombay", 1, "Maharashtra",
            "https://example.com/logo1.png", "https://example.com/logo1.png"),
        College("IIT Delhi", 2, "Delhi",
            "https://example.com/logo2.png", "https://example.com/logo1.png"),
        College("IIT Kanpur", 3, "Uttar Pradesh",
            "https://example.com/logo3.png", "https://example.com/logo1.png")
        // Add more colleges here
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_college_list)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        collegeAdapter = CollegeAdapter(collegeList)
        recyclerView.adapter = collegeAdapter
    }
}
