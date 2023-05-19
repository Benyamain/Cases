package com.example.cases.activities.trash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cases.R
import com.example.cases.activities.trash.features.view.TrashCaseView
import com.example.cases.activities.home.MainActivity
import com.example.cases.activities.home.features.search.SearchCase
import com.example.cases.adapter.home.CasesAdapter
import com.example.cases.database.db.CaseDatabase
import com.example.cases.databinding.ActivityTrashCaseBinding
import com.example.cases.models.data.home.Case
import com.example.cases.models.vm.home.CaseViewModel

class TrashCase : AppCompatActivity(), CasesAdapter.CasesClickListener,
    PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityTrashCaseBinding
    private lateinit var database: CaseDatabase
    lateinit var viewModel: CaseViewModel
    lateinit var adapter: CasesAdapter
    lateinit var selectedCase: Case
    lateinit var toggle: ActionBarDrawerToggle

    private val updateCase =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val case = result.data?.getSerializableExtra("case") as? Case

                if (case != null) {
                    viewModel.updateCase(case)
                }
            }
        }

    // When user taps on fab, we retrieve the result code
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val case = result.data?.getSerializableExtra("case") as? Case

                if (case != null) {
                    viewModel.insertCase(case)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrashCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUI()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(CaseViewModel::class.java)
        viewModel.allCases.observe(this) { list ->
            list?.let {
                /*val intent = Intent()
                val caseList = intent.getSerializableExtra("trash_case") as? ArrayList<Case>*/
                adapter.updateList(list)
            }
        }

        database = CaseDatabase.getDatabase(this)
    }

    private fun initializeUI() {
        binding.trashRecyclerView.setHasFixedSize(true)
        binding.trashRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = CasesAdapter(this, this)
        binding.trashRecyclerView.adapter = adapter

        toggle = ActionBarDrawerToggle(
            this@TrashCase,
            binding.trashDrawer,
            binding.trashToolbar,
            R.string.open,
            R.string.close
        )
        binding.trashDrawer.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.trashNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.nav_download -> {}
                R.id.nav_login -> {}
                R.id.nav_settings -> {}
                R.id.nav_trash -> {
                    startActivity(Intent(this, TrashCase::class.java))
                }
            }

            true
        }

        binding.trashImageSearch.setOnClickListener {
            val intent = Intent(this, SearchCase::class.java)
            getContent.launch(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(case: Case) {
        val intent = Intent(this@TrashCase, TrashCaseView::class.java)
        intent.putExtra("current_case", case)
        updateCase.launch(intent)
    }

    override fun onLongItemClicked(case: Case, cardView: CardView) {
        selectedCase = case
        popupDisplay(cardView)
    }

    private fun popupDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@TrashCase)
        popup.inflate(R.menu.trash_popup_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.trash_delete_case) {
            viewModel.deleteCase(selectedCase)

            return true
        } else if (item?.itemId == R.id.trash_restore_case) {
            val intent = Intent(this, SearchCase::class.java)
            getContent.launch(intent)

            return true
        }

        return false
    }
}