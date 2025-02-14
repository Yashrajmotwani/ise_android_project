package com.ourapp.ise_app_dev

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CollegeAdapter(private val collegeList: List<College>) : RecyclerView.Adapter<CollegeAdapter.CollegeViewHolder>() {

    inner class CollegeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val collegeLogo: ImageView = itemView.findViewById(R.id.collegeLogo)
        val collegeName: TextView = itemView.findViewById(R.id.collegeName)
        val founded: TextView = itemView.findViewById(R.id.founded)
        val num_faculty: TextView = itemView.findViewById(R.id.num_faculty)
        val num_student: TextView = itemView.findViewById(R.id.num_students)
        val stateLocation: TextView = itemView.findViewById(R.id.state)
        val collegeCard: CardView = itemView.findViewById(R.id.college_card)

        init {
            // Set the click listener on the card
            collegeCard.setOnClickListener {
                val college = collegeList[absoluteAdapterPosition]
                if (college.Website.isNotEmpty()) {
                    val url = if (!college.Website.startsWith("http://") &&
                        !college.Website.startsWith("https://")) {
                        "https://" + college.Website // Prepend "https://" if the URL doesn't already have it
                    } else {
                        college.Website // Use the URL as-is if it already has "http://" or "https://"
                    }

                    openCollegeWebsite(url)
                }
            }
        }

        private fun openCollegeWebsite(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollegeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CollegeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollegeViewHolder, position: Int) {
        val college = collegeList[position]

        // Bind the data from the College object to the views
        holder.collegeName.text = college.Name
        holder.founded.text = "Founded in: ${college.Founded}"
        holder.stateLocation.text = "State: ${college.stateUT}"
        holder.num_faculty.text = "Number of Faculty: ${college.Faculty}"
        holder.num_student.text = "Number of Students: ${college.Students}"

        // Set college logo using Glide
        Glide.with(holder.itemView.context)
            .load(college.Logo)  // College logo URL
            .into(holder.collegeLogo)

    }

    override fun getItemCount(): Int = collegeList.size
}
