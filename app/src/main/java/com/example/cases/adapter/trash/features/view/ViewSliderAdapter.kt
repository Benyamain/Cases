package com.example.cases.adapter.trash.features.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.cases.R
import com.example.cases.adapter.home.features.add.SliderAdapter
import com.example.cases.models.data.home.features.add.Slider

class ViewSliderAdapter(private val context: Context, private val list: List<Slider>): RecyclerView.Adapter<ViewSliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(LayoutInflater.from(context).inflate(R.layout.slider_list, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val slider = list[position]

        holder.topText.text = slider.topText
        holder.topText.isSelected = true
    }

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topText = itemView.findViewById<TextView>(R.id.sliderTv)
    }
}