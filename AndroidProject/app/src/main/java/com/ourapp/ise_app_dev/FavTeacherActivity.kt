package com.ourapp.ise_app_dev

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.ourapp.ise_app_dev.databinding.ActivityFavteacherBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavTeacherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavteacherBinding
    private lateinit var favoriteAdapter: FavTeacherAdapter
    private var favoriteTeacher: MutableList<Teacher> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavteacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        binding.recyclerViewFavTeachers.layoutManager = LinearLayoutManager(this)

        // Fetch user ID from Firebase
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            // Fetch favorite teachers from the server
            fetchFavoriteTeacher(userId)
        }

        // Set up the adapter
        favoriteAdapter = FavTeacherAdapter(favoriteTeacher) { teacher ->
            // Handle card click (show dialog with teacher details)
            showTeacherDetailsDialog(teacher)
        }
        binding.recyclerViewFavTeachers.adapter = favoriteAdapter
    }

    private fun fetchFavoriteTeacher(userId: String) {
        RetrofitClient.api.getFavoriteTeacher(userId).enqueue(object : Callback<List<Teacher>> {
            override fun onResponse(call: Call<List<Teacher>>, response: Response<List<Teacher>>) {
                if (response.isSuccessful) {
                    val teachers = response.body()
                    if (teachers != null) {
                        favoriteTeacher.clear()
                        favoriteTeacher.addAll(teachers)
                        favoriteAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@FavTeacherActivity, "Failed to load favorites", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Teacher>>, t: Throwable) {
                Toast.makeText(this@FavTeacherActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showTeacherDetailsDialog(teacher: Teacher) {

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        // Create and show the dialog displaying the teacher details
        val dialogView = layoutInflater.inflate(R.layout.dialog_teacher_details, null)

        // Show the dialog
        val alertDialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setTitle("Teacher Details")
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Remove") { dialog, _ ->
                userId?.let { userId ->
                    RetrofitClient.api.removeFavoriteTeacher(userId, teacher._id).enqueue(object :
                        Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(applicationContext, "Teacher Profile Removed!", Toast.LENGTH_SHORT).show()

                                // Restart the activity to refresh the UI
                                val intent = intent // Get the current intent
                                finish() // Finish the current activity
                                startActivity(intent) // Start the same activity again

                                dialog.dismiss()
                            } else {
                                Toast.makeText(applicationContext, "Teacher not Saved!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
            .create()

        // Set the full details of the teacher in the dialog
        val teacherImageView = dialogView.findViewById<ImageView>(R.id.teacherImage)
        Glide.with(teacherImageView.context)
            .load(teacher.image_link)  // Assuming 'teacher.imageUrl' contains the image URL
            .placeholder(R.drawable.baseline_face_24)  // Placeholder image while loading
            .error(R.drawable.baseline_face_24)  // Fallback image if loading fails
            .into(teacherImageView)  // Set the image into the ImageView

        // Set the full details of the teacher in the dialog
        dialogView.findViewById<TextView>(R.id.teacherName).text = "Faculty Name: ${teacher.name}"
        dialogView.findViewById<TextView>(R.id.position).text = "Position: ${teacher.position}"
        dialogView.findViewById<TextView>(R.id.qualification).text = "Qualification: ${teacher.qualification}"
        dialogView.findViewById<TextView>(R.id.areaInterest).text = "Areas of Interest: ${teacher.areas_of_interest}"
        dialogView.findViewById<TextView>(R.id.phone).text = "Phone: ${teacher.phone}"
        dialogView.findViewById<TextView>(R.id.college).text = "College: ${teacher.college}"
        dialogView.findViewById<TextView>(R.id.department).text = "Department: ${teacher.department}"

        // Set the email link to make it clickable
        val teacherLinkTextView = dialogView.findViewById<TextView>(R.id.emailID)
        val email = teacher.email
        if (email.isNullOrEmpty()) {
            teacherLinkTextView.text = "Email: NA"
            teacherLinkTextView.isClickable = false // Make it non-clickable
        } else {
            teacherLinkTextView.text = "Email: $email"
            teacherLinkTextView.setMovementMethod(LinkMovementMethod.getInstance()) // Make it clickable

            // Handle email click to open Gmail
            teacherLinkTextView.setOnClickListener {
                // Validate the email format using regex
                if (isValidEmail(email)) {
                    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Re: Your work in <Add Area of Interest>")
                    startActivity(Intent.createChooser(emailIntent, "Send Email"))
                } else {
                    // Show a message if the email is invalid
                    Toast.makeText(applicationContext, "Invalid email address", Toast.LENGTH_SHORT).show()
                }
            }
        }

        alertDialog.show()
    }

    fun isValidEmail(email: String?): Boolean {
        if (email.isNullOrEmpty()) return false

        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}"
        return email.matches(Regex(emailPattern))
    }

}