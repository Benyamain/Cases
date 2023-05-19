package com.example.cases.activities.trash.features.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.cases.databinding.ActivityTrashCaseViewBinding
import com.example.cases.models.data.home.Case
import com.example.cases.models.data.trash.Trash
import java.util.*

class ViewTrashCase : AppCompatActivity() {

    private lateinit var binding: ActivityTrashCaseViewBinding
    private lateinit var oldTrash: Trash

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
    }
}