package com.example.firsttry

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AchievementsAdapter(var achievements: List<Achievements>, var context: Context) : RecyclerView.Adapter<AchievementsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val header: TextView = view.findViewById(R.id.news_activity_header)
        val description: TextView = view.findViewById(R.id.news_activity_description)
        val pointSum: TextView = view.findViewById(R.id.news_activity_points)
        val goToNews: Button = view.findViewById(R.id.news_list_goToNews)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return achievements.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.header.text = achievements[position].title
        holder.description.text = achievements[position].description
        val points = achievements[position].points
        holder.pointSum.text = "Колличество баллов: $points"

        holder.goToNews.setOnClickListener {
            val intent = Intent(context, AchievementsActivity::class.java)

            intent.putExtra("title", achievements[position].title)
            intent.putExtra("description", achievements[position].description)
            intent.putExtra("points", achievements[position].points)
            intent.putExtra("id", achievements[position].id)

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}