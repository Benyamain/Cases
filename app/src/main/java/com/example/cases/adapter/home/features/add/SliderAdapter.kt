package com.example.cases.adapter.home.features.add

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.cases.R
import com.example.cases.models.data.home.features.add.Slider

class SliderAdapter(private val context: Context, private val list: List<Slider>): RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(LayoutInflater.from(context).inflate(R.layout.slider_list, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getSliderValues(): List<String?> {
        val sliderValues = mutableListOf<String?>()
        for (i in list.indices) {
            sliderValues.add(list[i].sliderValue)
        }

        return sliderValues
    }


    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val slider = list[position]

        holder.topText.text = slider.topText
        holder.topText.isSelected = true
        holder.slider.value = slider.sliderValue?.toFloat() ?: 0f
        holder.slider.isSelected = true
        holder.slider.addOnChangeListener { slider, value, fromUser ->
            slider.value = value
        }
    }

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topText = itemView.findViewById<TextView>(R.id.sliderTv)
        val slider = itemView.findViewById<com.google.android.material.slider.Slider>(R.id.sliderOne)
    }
}