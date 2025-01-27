package com.ourapp.ise_app_dev

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ourapp.ise_app_dev.databinding.FragmentSearchBinding
import com.ourapp.ise_app_dev.databinding.ItemProjectBinding

class TeacherAdapter(private val teachers: List<Teacher>) : RecyclerView.Adapter<TeacherAdapter.ProjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val teacher = teachers[position]
        holder.bind(teacher)
    }

    override fun getItemCount(): Int {
        return teachers.size
    }

    inner class ProjectViewHolder(private val binding: ItemProjectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teacher: Teacher) {
            binding.projectTitle.text = teacher.name
            binding.discipline.text = teacher.areas_of_interest
            binding.postingDate.text = teacher.college
            binding.department.text = teacher.department
            // Set other project details if available
        }
    }
}
