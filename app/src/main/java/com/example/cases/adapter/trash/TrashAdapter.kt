package com.example.cases.adapter.trash

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.cases.R
import com.example.cases.models.data.trash.Trash
import kotlin.random.Random

class TrashAdapter(private val context: Context, val listener: CasesClickListener): RecyclerView.Adapter<TrashAdapter.CaseViewHolder>() {


    private val trashList = ArrayList<Trash>()
    private val databaseList = ArrayList<Trash>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        return CaseViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return trashList.size
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        val currentCase = trashList[position]

        holder.title.text = currentCase.title
        holder.title.isSelected = true
        holder.case.text = currentCase.databaseCase
        holder.date.text = currentCase.date
        holder.date.isSelected = true
        holder.casesLayout.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.case_color_1, null))

        holder.casesLayout.setOnClickListener {
            listener.onItemClicked(trashList[holder.adapterPosition])
        }

        holder.casesLayout.setOnLongClickListener {
            listener.onLongItemClicked(trashList[holder.adapterPosition], holder.casesLayout)
            true
        }
    }

    fun updateList(newList: List<Trash>) {
        databaseList.clear()
        databaseList.addAll(newList)

        trashList.clear()
        trashList.addAll(databaseList)

        notifyDataSetChanged()
    }

    fun filterList(search: String) {
        trashList.clear()

        for (item in databaseList) {
            if (item.title?.lowercase()?.contains(search.lowercase()) == true || item.databaseCase?.lowercase()?.contains(search.lowercase()) == true) {
                trashList.add(item)
            }
        }

        notifyDataSetChanged()
    }

    fun randomColor(): Int {
        val list = ArrayList<Int>()

        list.add(R.color.case_color_1)
        list.add(R.color.case_color_2)
        list.add(R.color.case_color_3)
        list.add(R.color.case_color_4)
        list.add(R.color.case_color_5)
        list.add(R.color.case_color_6)

        val randomSeed = System.currentTimeMillis().toInt()
        val randomIndex = Random(randomSeed).nextInt(list.size)

        return list[randomIndex]
    }

    inner class CaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val casesLayout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_case_title)
        val case = itemView.findViewById<TextView>(R.id.tv_case_information)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
    }

    interface CasesClickListener {

        fun onItemClicked(trash: Trash)

        fun onLongItemClicked(trash: Trash, cardView: CardView)
    }
}