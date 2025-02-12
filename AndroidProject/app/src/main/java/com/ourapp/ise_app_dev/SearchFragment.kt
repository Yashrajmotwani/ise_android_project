package com.ourapp.ise_app_dev

import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.ourapp.ise_app_dev.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSearchBinding.inflate(inflater, container, false)

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe the search results
        searchViewModel.searchResults.observe(viewLifecycleOwner, Observer { projects ->
            // Update RecyclerView with search results
            val adapter = ProjectAdapter(projects) { project ->
                // Show dialog with project details when an item is clicked
                showProjectDetailsDialog(project)
            }
            binding.recyclerView.adapter = adapter
        })

        // Listen for search query
        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            if (query.isNotEmpty()) {
                // Call the search function
                searchViewModel.search(query)
            } else {
                Toast.makeText(context, "Please enter a query", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun showProjectDetailsDialog(project: Project) {
        // Create and display the dialog with project details
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_project_details, null)

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setTitle("Project Details")
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        // Set the full details of the project in the dialog
        dialogView.findViewById<TextView>(R.id.projectName).text = "Project Name: ${project.name_of_post}"
        dialogView.findViewById<TextView>(R.id.pi_name).text = "PI Name: ${project.pi_name}"
        dialogView.findViewById<TextView>(R.id.projectStatus).text = "Status: ${project.status}"
        dialogView.findViewById<TextView>(R.id.projectDiscipline).text = "Discipline: ${project.discipline}"
        dialogView.findViewById<TextView>(R.id.projectDate).text = "Posting Date: ${project.posting_date}"
        dialogView.findViewById<TextView>(R.id.lastDate).text = "Last Date: ${project.last_date}"
        dialogView.findViewById<TextView>(R.id.college).text = "College: ${project.college}"


        // Set the advertisement link (make it clickable)
        val projectLinkTextView = dialogView.findViewById<TextView>(R.id.projectLink)
        projectLinkTextView.text = project.advertisement_link
        projectLinkTextView.maxLines = 1
        projectLinkTextView.ellipsize = TextUtils.TruncateAt.END
        projectLinkTextView.setMovementMethod(LinkMovementMethod.getInstance())

        // Initialize Save and Remove buttons (ImageView buttons)
        val saveButton: Button = dialogView.findViewById(R.id.save_button)
        val removeButton: Button = dialogView.findViewById(R.id.remove_button)

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        // Handle Save button click
        saveButton.setOnClickListener {
            userId?.let { userId ->
                RetrofitClient.api.saveFavorite(userId, project).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Project Saved!", Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                        } else {
                            Toast.makeText(context, "Failed to save project", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        // Handle Remove button click
        removeButton.setOnClickListener {
            userId?.let { userId ->
                RetrofitClient.api.removeFavorite(userId, project._id).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Project Removed!", Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                        } else {
                            Toast.makeText(context, "Failed to remove project", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        alertDialog.show()
    }

}