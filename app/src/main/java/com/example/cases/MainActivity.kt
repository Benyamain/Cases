package com.example.cases

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
import com.example.cases.adapter.CasesAdapter
import com.example.cases.database.CaseDatabase
import com.example.cases.databinding.ActivityMainBinding
import com.example.cases.models.Case
import com.example.cases.models.CaseViewModel

class MainActivity : AppCompatActivity(), CasesAdapter.CasesClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: CaseDatabase
    lateinit var viewModel: CaseViewModel
    lateinit var adapter: CasesAdapter
    lateinit var selectedCase: Case

    private val updateCase = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val case = result.data?.getSerializableExtra("case") as? Case

            if (case != null) {
                viewModel.updateCase(case)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUI()

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(CaseViewModel::class.java)
        viewModel.allCases.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }

        database = CaseDatabase.getDatabase(this)
    }

    private fun initializeUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = CasesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        // When user taps on fab, we retrieve the result code
        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val case = result.data?.getSerializableExtra("case") as? Case

                if (case != null) {
                    viewModel.insertCase(case)
                }
            }
        }

        binding.fbAddCase.setOnClickListener {
            val intent = Intent(this, AddCase::class.java)
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

    override fun onItemClicked(case: Case) {
        val intent = Intent(this@MainActivity, AddCase::class.java)
        intent.putExtra("current_case", case)
        updateCase.launch(intent)
    }

    override fun onLongItemClicked(case: Case, cardView: CardView) {
        selectedCase = case
        popupDisplay(cardView)
    }

    private fun popupDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.popup_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_case) {
            viewModel.deleteCase(selectedCase)

            return true
        }

        return false
    }
}