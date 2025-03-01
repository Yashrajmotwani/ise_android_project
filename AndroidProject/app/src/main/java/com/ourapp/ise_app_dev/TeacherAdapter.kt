package com.ourapp.ise_app_dev

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ourapp.ise_app_dev.databinding.ItemTeacherBinding
import android.text.TextUtils

class TeacherAdapter(
    private val teachers: List<Teacher>,
    private val onProjectClick: (Teacher) -> Unit
) : RecyclerView.Adapter<TeacherAdapter.ProjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ItemTeacherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val teacher = teachers[position]
        holder.bind(teacher)
    }

    override fun getItemCount(): Int {
        return teachers.size
    }

    inner class ProjectViewHolder(private val binding: ItemTeacherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teacher: Teacher) {

            val email = if (teacher.email.isNullOrEmpty()) "NA" else teacher.email
            val dept = if (teacher.department.isNullOrEmpty()) "NA" else teacher.department

            binding.teacherName.text = teacher.name
            binding.areaInterest.text = teacher.areas_of_interest
            binding.college.text = "College: ${teacher.college}"
            binding.emailID.text = "Email: ${email}"
            binding.department.text = "Dept: ${dept}"


            // Set truncated Area of Interest
            binding.areaInterest.maxLines = 1
            binding.areaInterest.ellipsize = TextUtils.TruncateAt.END

            // Use Glide to load the teacher's image from a URL
            Glide.with(binding.teacherImage.context)
                .load(teacher.image_link)  // Assuming 'teacher.imageUrl' contains the image URL
                .placeholder(R.drawable.baseline_face_24)  // Placeholder image while loading
                .error(R.drawable.baseline_face_24)  // Fallback image if loading fails
                .into(binding.teacherImage)  // Set the image into the ImageView

            binding.root.setOnClickListener {
                onProjectClick(teacher) // When the item is clicked, trigger the callback
            }

        }
    }
}
