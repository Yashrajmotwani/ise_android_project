package com.ourapp.ise_app_dev

import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.ourapp.ise_app_dev.databinding.ActivityFavoriteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private var favoriteProjects: MutableList<Project> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
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
        favoriteAdapter = FavoriteAdapter(favoriteProjects) { project ->
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
                    Toast.makeText(this@FavoriteActivity, "Failed to load favorites", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                Toast.makeText(this@FavoriteActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showProjectDetailsDialog(project: Project) {
        // Create and show the dialog displaying the project details
        val dialogView = layoutInflater.inflate(R.layout.dialog_project_details, null)

        // Show the dialog
        val alertDialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setTitle("Project Details")
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
            .create()

        // Find and set the project data in the dialog
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

        // Set the Save/Remove button logic here
        val saveButton: Button = dialogView.findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            Toast.makeText(this, "Project is already Saved!", Toast.LENGTH_SHORT).show()
        }

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        val removeButton: Button = dialogView.findViewById(R.id.remove_button)
        // Handle Remove button click
        removeButton.setOnClickListener {
            userId?.let { userId ->
                RetrofitClient.api.removeFavorite(userId, project._id).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Project Removed!", Toast.LENGTH_SHORT).show()

                            // Restart the activity to refresh the UI
                            val intent = intent // Get the current intent
                            finish() // Finish the current activity
                            startActivity(intent) // Start the same activity again

                            alertDialog.dismiss()
                        } else {
                            Toast.makeText(applicationContext, "Failed to remove project", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        alertDialog.show()
    }

}
