package com.example.cases.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cases.R
import com.example.cases.adapter.CasesAdapter
import com.example.cases.database.CaseDatabase
import com.example.cases.databinding.ActivitySearchCaseBinding
import com.example.cases.models.Case
import com.example.cases.models.CaseViewModel
import java.util.*

class SearchCase : AppCompatActivity(), CasesAdapter.CasesClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivitySearchCaseBinding
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUI()
    }

    private fun initializeUI() {
        binding.searchRecyclerView.setHasFixedSize(true)
        binding.searchRecyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = CasesAdapter(this, this)
        binding.searchRecyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel = ViewModelProvider(this@SearchCase, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(CaseViewModel::class.java)
                    viewModel.allCases.observe(this@SearchCase) { list ->
                        list?.let {
                            for (item in list) {
                                if (item.title?.lowercase()?.contains(newText.lowercase()) == true || item.databaseCase?.lowercase()?.contains(newText.lowercase()) == true) {
                                    database = CaseDatabase.getDatabase(this@SearchCase)
                                    adapter.updateList(list)
                                }
                            }
                        }
                    }

                    adapter.filterList(newText)
                }

                return true
            }

        })
    }

    override fun onItemClicked(case: Case) {
        val intent = Intent(this@SearchCase, AddCase::class.java)
        intent.putExtra("current_case", case)
        updateCase.launch(intent)
    }

    override fun onLongItemClicked(case: Case, cardView: CardView) {
        selectedCase = case
        popupDisplay(cardView)
    }

    private fun popupDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@SearchCase)
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