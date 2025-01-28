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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            val adapter = TeacherAdapter(teachers) { teacher ->
                // Show dialog with project details when an item is clicked
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
        // Create and display the dialog with project details
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_teacher_details, null)

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setTitle("Faculty Details")
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        // Set the full details of the project in the dialog
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
        dialogView.findViewById<TextView>(R.id.phone).text = "Phone: ${teacher.phone}"
        dialogView.findViewById<TextView>(R.id.college).text = "College: ${teacher.college}"
        dialogView.findViewById<TextView>(R.id.department).text = "Department: ${teacher.department}"

        // Set the email link to make it clickable
        val teacherLinkTextView = dialogView.findViewById<TextView>(R.id.emailID)
        teacherLinkTextView.text = "Email: ${teacher.email}"
        teacherLinkTextView.setMovementMethod(LinkMovementMethod.getInstance())

        // Handle email click to open Gmail
        teacherLinkTextView.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${teacher.email}"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Re: Your work in <Add Area of Interest>")
            startActivity(Intent.createChooser(emailIntent, "Send Email"))
        }

        alertDialog.show()
    }

}