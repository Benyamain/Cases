package com.example.notes

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes.adapter.ScoresAdapter
import com.example.notes.database.ScoreDatabase
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.models.Score

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var datatbase: ScoreDatabase
    lateinit var viewModel: ScoreViewModel
    lateinit var adapter: ScoresAdapter
    lateinit var selectedScore: Score


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUI()

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(ScoreViewModel::class.java)
        viewModel.allScores.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }

        datatbase = ScoreDatabase.getDatabase(this)
    }

    private fun initializeUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = ScoresAdapter(this, this)
        binding.recyclerView.adapter = adapter

        // When user taps on fab, we retrieve the result code
        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val score = result.data?.getSerializableExtra("score") as? Score

                if (score != null) {

                }
            }
        }
    }
}