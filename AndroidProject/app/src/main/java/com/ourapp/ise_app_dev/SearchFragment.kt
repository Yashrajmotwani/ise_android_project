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
import java.text.SimpleDateFormat
import java.util.Locale


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

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        // Create and display the dialog with project details
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_project_details, null)

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setTitle("Project Details")
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Save") { dialog, _ ->
                userId?.let { userId ->
                    RetrofitClient.api.saveFavorite(userId, project).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Project Saved!", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
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
            .setNegativeButton("Remove") { dialog, _ ->
                userId?.let { userId ->
                    RetrofitClient.api.removeFavorite(userId, project._id).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Project Removed!", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            } else {
                                Toast.makeText(context, "Project not Saved!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
            .create()

        // Check if discipline, pi_name is empty and set it to "NA" if so
        val discipline = if (project.discipline.isNullOrEmpty()) "NA" else project.discipline
        val pi_name = if (project.pi_name.isNullOrEmpty()) "NA" else project.pi_name

        var status = project.status ?: "" // Safe call to handle null
        if (status.isNullOrEmpty() || (status != "Open" && status != "Closed")) {
            // Convert the last_date string to a timestamp for comparison
            val lastDate = project.last_date?.let { parseDate(it) } ?: 0L // Safe call for null last_date

            // Get the current date's timestamp
            val currentDate = System.currentTimeMillis() // Current timestamp

            // Compare last date with current date to determine status
            status = if (lastDate > currentDate) {
                "Closed" // If last date is in the future, status is "Closed"
            } else {
                "Open" // If last date is in the past or equal to current date, status is "Open"
            }
        }


        // Set the full details of the project in the dialog
        dialogView.findViewById<TextView>(R.id.projectName).text = "Project Name: ${project.name_of_post}"
        dialogView.findViewById<TextView>(R.id.pi_name).text = "PI Name: ${pi_name}"
        dialogView.findViewById<TextView>(R.id.projectStatus).text = "Status: ${status}"
        dialogView.findViewById<TextView>(R.id.projectDiscipline).text = "Discipline: ${discipline}"
        dialogView.findViewById<TextView>(R.id.projectDate).text = "Posting Date: ${project.posting_date}"
        dialogView.findViewById<TextView>(R.id.lastDate).text = "Last Date: ${project.last_date}"
        dialogView.findViewById<TextView>(R.id.college).text = "College: ${project.college}"


        // Set the advertisement link (make it clickable)
        val projectLinkTextView = dialogView.findViewById<TextView>(R.id.projectLink)
        projectLinkTextView.text = project.advertisement_link
        projectLinkTextView.maxLines = 1
        projectLinkTextView.ellipsize = TextUtils.TruncateAt.END
        projectLinkTextView.setMovementMethod(LinkMovementMethod.getInstance())


        alertDialog.show()
    }

    private fun parseDate(dateString: String): Long {
        // List of possible date formats to try
        val formats = listOf(
            "dd.MM.yyyy",  // Example: 11.02.2025
            "yyyy-MM-dd",  // Example: 2025-02-11
            "MM/dd/yyyy",  // Example: 02/11/2025
            "yyyy/MM/dd",  // Example: 2025/02/11
            "dd/MM/yyyy",  // Example: 11/02/2025
            "MMMM d, yyyy", // Example: February 11, 2025
            "d MMMM, yyyy"  // Example: 11 February, 2025
        )

        val locale = Locale.getDefault()

        // Try to parse the date with each format in the list
        for (format in formats) {
            val formatter = SimpleDateFormat(format, locale)
            try {
                val date = formatter.parse(dateString)
                if (date != null) {
                    return date.time // Return the time in milliseconds
                }
            } catch (e: Exception) {
                // If parsing fails, continue with the next format
            }
        }

        return 0L // Return 0 if no format works
    }

}