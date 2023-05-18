package com.example.notes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes.adapter.ScoresAdapter
import com.example.notes.database.ScoreDatabase
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.models.Score
import com.example.notes.models.ScoreViewModel

class MainActivity : AppCompatActivity(), ScoresAdapter.ScoresClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: ScoreDatabase
    lateinit var viewModel: ScoreViewModel
    lateinit var adapter: ScoresAdapter
    lateinit var selectedScore: Score

    private val updateScore = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val score = result.data?.getSerializableExtra("score") as? Score

            if (score != null) {
                viewModel.updateScore(score)
            }
        }
    }

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

        database = ScoreDatabase.getDatabase(this)
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
                    viewModel.insertScore(score)
                }
            }
        }

        binding.fbAddScore.setOnClickListener {
            val intent = Intent(this, AddScore::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filterList(newText)
                }

                return true
            }

        })
    }

    override fun onItemClicked(score: Score) {
        val intent = Intent(this@MainActivity, AddScore::class.java)
        intent.putExtra("currentScore", score)
        updateScore.launch(intent)
    }

    override fun onLongItemClicked(score: Score, cardView: CardView) {
        selectedScore = score
        popupDisplay(cardView)
    }

    private fun popupDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.popup_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_score) {
            viewModel.deleteScore(selectedScore)

            return true
        }

        return false
    }
}