package com.example.notes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notes.databinding.ActivityAddScoreBinding
import com.example.notes.models.Score
import java.text.SimpleDateFormat
import java.util.*

class AddScore : AppCompatActivity() {

    private lateinit var  binding: ActivityAddScoreBinding
    private lateinit var score: Score
    private lateinit var oldScore: Score
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldScore = intent.getSerializableExtra("currentScore") as Score
            binding.etTitle.setText(oldScore.title)
            binding.etScore.setText(oldScore.score)
            isUpdate = true
        } catch(e: Exception) {
            e.printStackTrace()
        }

        binding.imageCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val scoreDescription = binding.etScore.text.toString()

            if (title.isNotEmpty() || scoreDescription.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

                if (isUpdate) {
                    score = Score(oldScore.id, title, scoreDescription, formatter.format(Date()))
                } else {
                    score = Score(null, title, scoreDescription, formatter.format(Date()))
                }

                val intent = Intent()
                intent.putExtra("score", score)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@AddScore, "Please enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.imageBackArrow.setOnClickListener {
            onBackPressed()
        }
    }
}