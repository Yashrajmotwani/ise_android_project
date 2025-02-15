package com.ourapp.ise_app_dev

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.ourapp.ise_app_dev.databinding.FragmentTeacherBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            val adapter = TeacherAdapter(teachers) { teacher ->
                // Show dialog with teacher details when an item is clicked
                showTeacherDetailsDialog(teacher)
            }
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

    private fun showTeacherDetailsDialog(teacher: Teacher) {

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        // Create and display the dialog with teacher details
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_teacher_details, null)

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setTitle("Faculty Details")
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Save") { dialog, _ ->
                userId?.let { userId ->
                    RetrofitClient.api.saveFavoriteTeacher(userId, teacher).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Teacher Profile Saved!", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            } else {
                                Toast.makeText(context, "Failed to save Teacher Profile", Toast.LENGTH_SHORT).show()
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
                    RetrofitClient.api.removeFavoriteTeacher(userId, teacher._id).enqueue(object :
                        Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Teacher Profile Removed!", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            } else {
                                Toast.makeText(context, "Teacher not Saved!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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

        dialogView.findViewById<TextView>(R.id.teacherName).text = "Faculty Name: ${teacher.name}"
        dialogView.findViewById<TextView>(R.id.position).text = "Position: ${teacher.position}"
        dialogView.findViewById<TextView>(R.id.qualification).text = "Qualification: ${teacher.qualification}"
        dialogView.findViewById<TextView>(R.id.areaInterest).text = "Areas of Interest: ${teacher.areas_of_interest}"
        dialogView.findViewById<TextView>(R.id.college).text = "College: ${teacher.college}"
        dialogView.findViewById<TextView>(R.id.department).text = "Department: ${teacher.department}"

        val phoneTextView = dialogView.findViewById<TextView>(R.id.phone)

        // Check if the phone number is null or empty
        val phone = teacher.phone
        if (phone.isNullOrEmpty() || phone == "N/A") {
            phoneTextView.text = "Phone: NA"
            phoneTextView.isClickable = false // Make it non-clickable
        } else {
            context?.let {
                phoneTextView.setTextColor(ContextCompat.getColor(it, android.R.color.holo_blue_dark))
            }
            phoneTextView.text = "Phone: $phone"
            phoneTextView.isClickable = true // Make it clickable

            // Handle phone number click to validate and initiate dialing
            phoneTextView.setOnClickListener {
                val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                startActivity(phoneIntent)
            }
        }

        // Set the email link to make it clickable
        val teacherLinkTextView = dialogView.findViewById<TextView>(R.id.emailID)
        val email = teacher.email
        if (email.isNullOrEmpty() || email == "N/A") {
            teacherLinkTextView.text = "Email: NA"
            teacherLinkTextView.isClickable = false // Make it non-clickable
        } else {
            context?.let {
                teacherLinkTextView.setTextColor(ContextCompat.getColor(it, android.R.color.holo_blue_dark))
            }
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
                    Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
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