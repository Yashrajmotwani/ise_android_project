package com.ourapp.ise_app_dev

import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.ourapp.ise_app_dev.databinding.ActivityFavprojectBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class FavProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavprojectBinding
    private lateinit var favoriteAdapter: FavProjectAdapter
    private var favoriteProjects: MutableList<Project> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavprojectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(this)

        // Fetch user ID from Firebase
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            // Fetch favorite projects from the server
            fetchFavoriteProjects(userId)
        }

        // Set up the adapter
        favoriteAdapter = FavProjectAdapter(favoriteProjects) { project ->
            // Handle card click (show dialog with project details)
            showProjectDetailsDialog(project)
        }
        binding.recyclerViewFavorites.adapter = favoriteAdapter
    }

    private fun fetchFavoriteProjects(userId: String) {
        RetrofitClient.api.getFavoriteProjects(userId).enqueue(object : Callback<List<Project>> {
            override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>) {
                if (response.isSuccessful) {
                    val projects = response.body()
                    if (projects != null) {
                        favoriteProjects.clear()
                        favoriteProjects.addAll(projects)
                        favoriteAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@FavProjectActivity, "No Favorites...", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                Toast.makeText(this@FavProjectActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showProjectDetailsDialog(project: Project) {

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        // Create and show the dialog displaying the project details
        val dialogView = layoutInflater.inflate(R.layout.dialog_project_details, null)

        // Show the dialog
        val alertDialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setTitle("Project Details")
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Remove") { dialog, _ ->
                userId?.let { userId ->
                    RetrofitClient.api.removeFavorite(userId, project._id).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(applicationContext, "Project Removed!", Toast.LENGTH_SHORT).show()

                                // Restart the activity to refresh the UI
                                val intent = intent // Get the current intent
                                finish() // Finish the current activity
                                startActivity(intent) // Start the same activity again

                                dialog.dismiss()
                            } else {
                                Toast.makeText(applicationContext, "Project not Saved!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
