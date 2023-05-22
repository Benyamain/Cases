package com.example.cases.activities.trash.features.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cases.adapter.home.features.add.SliderAdapter
import com.example.cases.adapter.trash.features.view.ViewSliderAdapter
import com.example.cases.databinding.ActivityTrashCaseViewBinding
import com.example.cases.models.data.home.Case
import com.example.cases.models.data.home.features.add.Slider
import com.example.cases.models.data.trash.Trash
import java.util.*

class ViewTrashCase : AppCompatActivity() {

    private lateinit var binding: ActivityTrashCaseViewBinding
    private lateinit var oldTrash: Trash
    lateinit var adapter: ViewSliderAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrashCaseViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldTrash = intent.getSerializableExtra("current_trash") as Trash
            binding.vtcEtTitle.setText(oldTrash.title)
            binding.vtcEtCase.setText(oldTrash.databaseCase)
        } catch(e: Exception) {
            e.printStackTrace()
        }

        binding.vtcImageBackArrow.setOnClickListener {
            onBackPressed()
        }

        initializeUI()
    }

    private fun initializeUI() {
        val slider = ArrayList<Slider>()
        slider.add(Slider("Extremity", null))
        slider.add(Slider("Narcissism", null))
        slider.add(Slider("Anxiety", null))
        slider.add(Slider("Assertiveness", null))
        slider.add(Slider("Openness to Experience", null))
        slider.add(Slider("Empathy", null))
        slider.add(Slider("Impulsivity", null))
        slider.add(Slider("Self-Control", null))

        binding.vtcSliderRv.setHasFixedSize(true)
        binding.vtcSliderRv.layoutManager = LinearLayoutManager(this)
        adapter = ViewSliderAdapter(this, slider)
        binding.vtcSliderRv.adapter = adapter
    }
}