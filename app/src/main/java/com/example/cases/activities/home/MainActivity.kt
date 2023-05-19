package com.example.cases.activities.home

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cases.R
import com.example.cases.activities.home.features.search.SearchCase
import com.example.cases.activities.trash.TrashCase
import com.example.cases.activities.home.features.add.AddCase
import com.example.cases.adapter.home.CasesAdapter
import com.example.cases.database.db.CaseDatabase
import com.example.cases.databinding.ActivityMainBinding
import com.example.cases.models.data.home.Case
import com.example.cases.models.data.trash.Trash
import com.example.cases.models.vm.home.CaseViewModel
import com.example.cases.models.vm.trash.TrashViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MainActivity : AppCompatActivity(), CasesAdapter.CasesClickListener,
    PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: CaseDatabase
    lateinit var caseViewModel: CaseViewModel
    lateinit var trashViewModel: TrashViewModel
    lateinit var adapter: CasesAdapter
    lateinit var selectedCase: Case
    private lateinit var trash: Trash
    lateinit var toggle: ActionBarDrawerToggle

    private val updateCase =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val case = result.data?.getSerializableExtra("case") as? Case

                if (case != null) {
                    caseViewModel.updateCase(case)
                }
            }
        }

    // When user taps on fab, we retrieve the result code
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val case = result.data?.getSerializableExtra("case") as? Case

                if (case != null) {
                    caseViewModel.insertCase(case)
                }
            }
        }

    private val updateTrash =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val trashCase = result.data?.getSerializableExtra("trash") as? Trash

                if (trashCase != null) {
                    trashViewModel.updateTrash(trashCase)
                }
            }
        }

    private val getTrashContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val trash = result.data?.getSerializableExtra("trash") as? Trash

                if (trash != null) {
                    trashViewModel.insertTrash(trash)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUI()

        caseViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(CaseViewModel::class.java)
        caseViewModel.allCases.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }

        database = CaseDatabase.getDatabase(this)
    }

    private fun initializeUI() {
        binding.homeRecyclerView.setHasFixedSize(true)
        binding.homeRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = CasesAdapter(this, this)
        binding.homeRecyclerView.adapter = adapter

        toggle = ActionBarDrawerToggle(
            this@MainActivity,
            binding.drawer,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
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

        binding.fbAddCase.setOnClickListener {
            val intent = Intent(this, AddCase::class.java)
            getContent.launch(intent)
        }

        binding.imageSearch.setOnClickListener {
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
            val id = selectedCase.id
            val title = selectedCase.title
            val caseDescription = selectedCase.databaseCase
            val caseDate = selectedCase.date

            trash = Trash(id, title, caseDescription, caseDate)
            Log.d("Garbage", "$trash")

            val intent = Intent()
            intent.putExtra("trash", trash)
            Log.d("Garbage", "${intent.extras}")
            setResult(Activity.RESULT_OK, intent)


            caseViewModel.deleteCase(selectedCase)

            return true
        }

        return false
    }
}