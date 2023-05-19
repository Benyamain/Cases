package com.example.cases.activities.features.trash.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.cases.databinding.ActivityTrashCaseViewBinding
import com.example.cases.models.data.Case
import java.util.*

class TrashCaseView : AppCompatActivity() {

    private lateinit var binding: ActivityTrashCaseViewBinding
    private lateinit var oldCase: Case

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrashCaseViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldCase = intent.getSerializableExtra("current_case") as Case
            binding.tcvEtTitle.setText(oldCase.title)
            binding.tcvEtCase.setText(oldCase.databaseCase)
        } catch(e: Exception) {
            e.printStackTrace()
        }

        binding.tcvImageBackArrow.setOnClickListener {
            onBackPressed()
        }
    }
}