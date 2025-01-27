package com.ourapp.ise_app_dev

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ourapp.ise_app_dev.databinding.FragmentTeacherBinding

class TeacherFragment : Fragment() {

    private lateinit var teacherViewModel: TeacherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentTeacherBinding.inflate(inflater, container, false)

        teacherViewModel = ViewModelProvider(this).get(TeacherViewModel::class.java)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe the search results
        teacherViewModel.fsearchResults.observe(viewLifecycleOwner, Observer { teachers ->
            // Update RecyclerView with search results
            val adapter = TeacherAdapter(teachers)
            binding.recyclerView.adapter = adapter
        })

        // Listen for search query
        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            if (query.isNotEmpty()) {
                // Call the search function
                teacherViewModel.fsearch(query)
            } else {
                Toast.makeText(context, "Please enter a query", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}