package com.example.firsttry

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NewsAdapter(var news: List<News>, var context: Context) : RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

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
        return news.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.header.text = news[position].header
        holder.description.text = news[position].description
        val points = news[position].pointSum.toString()
        holder.pointSum.text = "Колличество баллов: $points"

        holder.goToNews.setOnClickListener {
            val intent = Intent(context, NewsActivity::class.java)

            intent.putExtra("header", news[position].header)
            intent.putExtra("description", news[position].description)
            intent.putExtra("userName", news[position].userName)
            intent.putExtra("pointSum", points)
            intent.putExtra("id", news[position].id.toString())
            intent.putExtra("readiness", news[position].readiness.toString())

            context.startActivity(intent)
        }
    }
}