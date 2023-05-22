package com.example.cases.activities.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cases.R
import com.example.cases.activities.home.features.add.AddCase
import com.example.cases.activities.home.features.search.SearchCase
import com.example.cases.activities.trash.TrashCase
import com.example.cases.adapter.home.CasesAdapter
import com.example.cases.database.db.CaseDatabase
import com.example.cases.databinding.ActivityMainBinding
import com.example.cases.models.data.home.Case
import com.example.cases.models.data.trash.Trash
import com.example.cases.models.vm.home.CaseViewModel
import com.example.cases.models.vm.trash.TrashViewModel
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException


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

    private fun commonDocumentDirPath(): File? {
        var dir: File? = null
        dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .toString() + "/cases"
            )
        } else {
            File(Environment.getExternalStorageDirectory().toString() + "/cases")
        }

        if (!dir.exists()) {
            val success = dir.mkdirs()
            if (!success) {
                dir = null
            }
        }
        return dir
    }

    private fun writeToFile(data: List<Case>, fileName: String) {
        val directory = commonDocumentDirPath()
        val file = File(directory, fileName)

        try {
            val writer = FileWriter(file)
            val buffer = BufferedWriter(writer)

            var isFirstLine = true

            for (dataPoint in data) {
                if (isFirstLine) {
                    buffer.write("\t\t\tHome\n\n${dataPoint.toString()}")
                    isFirstLine = false
                } else {
                    buffer.write(dataPoint.toString())
                }
                buffer.newLine()
            }

            buffer.close()
            writer.close()

            Toast.makeText(this, "File downloaded in Internal Storage Documents directory", Toast.LENGTH_LONG).show()

            Log.d("FileWrite", "Data written to $file")
        } catch (e: IOException) {
            Toast.makeText(this, "File error", Toast.LENGTH_SHORT).show()
            Log.e("FileWrite", "Error writing to file: ${e.message}")
        }
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
                R.id.nav_download -> {
                    caseViewModel.allCases.observe(this) { list ->
                        list?.let {
                            writeToFile(list, "cases.txt")
                        }
                    }
                }
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

            val s1 = selectedCase.sliderOne
            val s2 = selectedCase.sliderTwo
            val s3 = selectedCase.sliderThree
            val s4 = selectedCase.sliderFour
            val s5 = selectedCase.sliderFive
            val s6 = selectedCase.sliderSix
            val s7 = selectedCase.sliderSeven
            val s8 = selectedCase.sliderEight

            trash = Trash(id, title, caseDescription, caseDate, s1, s2, s3, s4, s5, s6, s7, s8)
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