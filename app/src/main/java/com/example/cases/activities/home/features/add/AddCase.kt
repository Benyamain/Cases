package com.example.cases.activities.home.features.add

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.cases.databinding.ActivityAddCaseBinding
import com.example.cases.models.data.home.Case
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class AddCase : AppCompatActivity() {

    private lateinit var binding: ActivityAddCaseBinding
    private lateinit var case: Case
    private lateinit var oldCase: Case
    var isUpdate = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldCase = intent.getSerializableExtra("current_case") as Case
            binding.etTitle.setText(oldCase.title)
            binding.etCase.setText(oldCase.databaseCase)
            isUpdate = true
        } catch(e: Exception) {
            e.printStackTrace()
        }

        binding.imageCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val caseDescription = binding.etCase.text.toString()

            if (title.isNotEmpty() || caseDescription.isNotEmpty()) {
                val current = LocalDateTime.now()

                val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                val formatted = current.format(formatter)

                if (isUpdate) {
                    case = Case(oldCase.id, title, caseDescription, formatted)
                } else {
                    case = Case(null, title, caseDescription, formatted)
                }

                val intent = Intent()
                intent.putExtra("case", case)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@AddCase, "Please enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.imageBackArrow.setOnClickListener {
            onBackPressed()
        }
    }
}