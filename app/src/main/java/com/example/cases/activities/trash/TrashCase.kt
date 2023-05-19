package com.example.cases.activities.trash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.cases.activities.trash.features.view.ViewTrashCase
import com.example.cases.activities.home.MainActivity
import com.example.cases.activities.home.features.search.SearchCase
import com.example.cases.activities.trash.features.search.SearchTrashCase
import com.example.cases.adapter.trash.TrashAdapter
import com.example.cases.database.db.CaseDatabase
import com.example.cases.databinding.ActivityTrashCaseBinding
import com.example.cases.models.data.trash.Trash
import com.example.cases.models.vm.trash.TrashViewModel

class TrashCase : AppCompatActivity(), TrashAdapter.CasesClickListener,
    PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityTrashCaseBinding
    private lateinit var database: CaseDatabase
    lateinit var viewModel: TrashViewModel
    lateinit var adapter: TrashAdapter
    lateinit var selectedTrash: Trash
    lateinit var toggle: ActionBarDrawerToggle

    private val updateTrash =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val trashCase = result.data?.getSerializableExtra("trash") as? Trash
                Log.d("TrashCase", "$trashCase")

                if (trashCase != null) {
                    viewModel.updateTrash(trashCase)
                }
            }
        }

    // When user taps on fab, we retrieve the result code
    private val getTrashContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val trashCase = result.data?.getSerializableExtra("trash") as? Trash
                Log.d("TrashCase", "$trashCase")

                if (trashCase != null) {
                    viewModel.insertTrash(trashCase)
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
        ).get(TrashViewModel::class.java)
        viewModel.allTrash.observe(this) { list ->
            list?.let {
                /*val intent = Intent()
                val caseList = intent.getSerializableExtra("trash_case") as? ArrayList<Case>*/
                adapter.updateList(list)
                Log.d("TrashCase", "onCreate: $list")
            }
        }

        database = CaseDatabase.getDatabase(this)
        Log.d("TrashCreate", "onCreate: $database")
    }

    private fun initializeUI() {
        binding.trashRecyclerView.setHasFixedSize(true)
        binding.trashRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = TrashAdapter(this, this)
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
            val intent = Intent(this, SearchTrashCase::class.java)
            getTrashContent.launch(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(trash: Trash) {
        val intent = Intent(this@TrashCase, ViewTrashCase::class.java)
        intent.putExtra("current_trash", trash)
        updateTrash.launch(intent)
    }

    override fun onLongItemClicked(trash: Trash, cardView: CardView) {
        selectedTrash = trash
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
            viewModel.deleteTrash(selectedTrash)

            return true
        } else if (item?.itemId == R.id.trash_restore_case) {
            /*Do something here*/
            val intent = Intent(this, SearchCase::class.java)
            getTrashContent.launch(intent)

            return true
        }

        return false
    }
}