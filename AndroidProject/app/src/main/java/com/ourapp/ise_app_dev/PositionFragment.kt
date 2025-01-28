package com.ourapp.ise_app_dev

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PositionFragment : Fragment(R.layout.fragment_position) {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PositionAdapter  // Adapter to display the positions list
    private var positionList = mutableListOf<String>() // List of positions (data)
    private var filteredPositionList = mutableListOf<String>() // List after filtering

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        searchView = view.findViewById(R.id.searchView)
        searchView.setIconified(false)
        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Sample data for positions (can be replaced with real data)
        positionList = mutableListOf(
            "Software Engineer",
            "Data Scientist",
            "AI Researcher",
            "Web Developer",
            "Android Developer"
        )
        filteredPositionList.addAll(positionList)

        // Set up RecyclerView and adapter
        adapter = PositionAdapter(filteredPositionList)  // Adapter to show filtered positions
        recyclerView.adapter = adapter

        // Set up SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false  // You can add logic to handle submit action if needed
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the list based on the input text
                filterPositions(newText)
                return true
            }
        })
    }

    private fun filterPositions(query: String?) {
        filteredPositionList.clear()  // Clear the previous filtered results

        if (query.isNullOrEmpty()) {
            filteredPositionList.addAll(positionList)  // If query is empty, show all items
        } else {
            // Filter the list based on the query
            positionList.forEach {
                if (it.contains(query, ignoreCase = true)) {
                    filteredPositionList.add(it)
                }
            }
        }

        adapter.notifyDataSetChanged()  // Notify adapter about the data change
    }
}
