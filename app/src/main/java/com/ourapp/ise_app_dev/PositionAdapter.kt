package com.ourapp.ise_app_dev

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PositionAdapter(private val positionList: List<String>) : RecyclerView.Adapter<PositionAdapter.PositionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return PositionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
        holder.bind(positionList[position])
    }

    override fun getItemCount(): Int {
        return positionList.size
    }

    inner class PositionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(position: String) {
            textView.text = position
        }
    }
}
