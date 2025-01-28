package com.ourapp.ise_app_dev

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.net.Uri

class CollegeAdapter(private val collegeList: List<College>) : RecyclerView.Adapter<CollegeAdapter.CollegeViewHolder>() {

    inner class CollegeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val collegeLogo: ImageView = itemView.findViewById(R.id.collegeLogo)
        val collegeName: TextView = itemView.findViewById(R.id.collegeName)
        val nirfRanking: TextView = itemView.findViewById(R.id.nirfRank)
        val stateLocation: TextView = itemView.findViewById(R.id.state)
        val collegeCard: CardView = itemView.findViewById(R.id.college_card)

        init {
            // Set the click listener on the card
            collegeCard.setOnClickListener {
                val college = collegeList[absoluteAdapterPosition]
                openCollegeWebsite(college.websiteUrl)
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
        holder.collegeName.text = college.name
        holder.nirfRanking.text = "NIRF Rank: ${college.nirfRank}"
        holder.stateLocation.text = "State: ${college.state}"

        // Set college logo (you can load images using Glide or Picasso)
        Glide.with(holder.itemView.context)
            .load(college.logoUrl)  // College logo URL or drawable resource
            .into(holder.collegeLogo)
    }

    override fun getItemCount(): Int = collegeList.size
}
