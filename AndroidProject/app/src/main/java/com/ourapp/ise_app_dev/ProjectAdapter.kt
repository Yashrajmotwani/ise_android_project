package com.ourapp.ise_app_dev

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ourapp.ise_app_dev.databinding.ItemProjectBinding

class ProjectAdapter(
    private val projects: List<Project>,
    private val onProjectClick: (Project) -> Unit) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.bind(project)
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    inner class ProjectViewHolder(private val binding: ItemProjectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(project: Project) {

            // Check if project.discipline is empty and set it to "NA" if so
            val discipline = if (project.discipline.isNullOrEmpty()) "NA" else project.discipline
            val lastdate = if (project.last_date.isNullOrEmpty() || project.last_date == "N/A") "NA" else project.last_date
            val projectname = if (project.name_of_post.isNullOrEmpty()) project.discipline else project.name_of_post

            binding.projectTitle.text = projectname
            binding.discipline.text = "Discipline: ${discipline}"
            binding.lastDate.text = "Last Date: ${lastdate}"
            binding.college.text = "College: ${project.college}"

            binding.root.setOnClickListener {
                onProjectClick(project) // When the item is clicked, trigger the callback
            }

        }
    }
}
