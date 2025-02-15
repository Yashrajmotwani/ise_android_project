package com.ourapp.ise_app_dev

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ourapp.ise_app_dev.databinding.ItemProjectBinding

class FavProjectAdapter(
    private val favoriteProjects: List<Project>,
    private val onProjectClick: (Project) -> Unit
) : RecyclerView.Adapter<FavProjectAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val project = favoriteProjects[position]
        holder.bind(project)
    }

    override fun getItemCount(): Int {
        return favoriteProjects.size
    }

    inner class FavoriteViewHolder(private val binding: ItemProjectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(project: Project) {

            // Check if project.discipline is empty and set it to "NA" if so
            val discipline = if (project.discipline.isNullOrEmpty()) "NA" else project.discipline

            // Set the project data in the views
            binding.projectTitle.text = project.name_of_post
            binding.discipline.text = "Discipline: ${discipline}"
            binding.lastDate.text = "Last Date: ${project.last_date}"
            binding.college.text = "College: ${project.college}"

            // Set the onClick listener for the entire card view to open the dialog
            binding.root.setOnClickListener {
                onProjectClick(project)
            }
        }
    }
}
