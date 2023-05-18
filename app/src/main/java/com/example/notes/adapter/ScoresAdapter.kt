package com.example.notes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.models.Score
import org.w3c.dom.Text
import kotlin.random.Random

class ScoresAdapter(private val context: Context): RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder> {

    private val scoresList: ArrayList<Score>()
    private val databaseList: ArrayList<Score>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        return ScoreViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return scoresList.size
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val currentScore = scoresList[position]
        holder.title.text = currentScore.title
        holder.title.isSelected = true
        holder.score.text = currentScore.score
        holder.date.text = currentScore.date
        holder.date.isSelected = true
    }

    fun randomColor(): Int {
        val list = ArrayList<Int>()

        list.add(R.color.score_color_1)
        list.add(R.color.score_color_2)
        list.add(R.color.score_color_3)
        list.add(R.color.score_color_4)
        list.add(R.color.score_color_5)
        list.add(R.color.score_color_6)

        val randomSeed = System.currentTimeMillis().toInt()
        val randomIndex = Random(randomSeed).nextInt(list.size)

        return list[randomIndex]
    }

    inner class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scoresLayout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val score = itemView.findViewById<TextView>(R.id.tv_score)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
    }
}