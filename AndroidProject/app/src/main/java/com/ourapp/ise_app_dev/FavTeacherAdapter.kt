package com.ourapp.ise_app_dev

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ourapp.ise_app_dev.databinding.ItemTeacherBinding

class FavTeacherAdapter(
    private val favoriteTeacher: List<Teacher>,
    private val onProjectClick: (Teacher) -> Unit
) : RecyclerView.Adapter<FavTeacherAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemTeacherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val teacher = favoriteTeacher[position]
        holder.bind(teacher)
    }

    override fun getItemCount(): Int {
        return favoriteTeacher.size
    }

    inner class FavoriteViewHolder(private val binding: ItemTeacherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teacher: Teacher) {

            binding.teacherName.text = teacher.name
            binding.areaInterest.text = teacher.areas_of_interest
            binding.college.text = teacher.college
            binding.emailID.text = teacher.email
            binding.department.text = teacher.department

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
