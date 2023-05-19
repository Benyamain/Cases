package com.example.cases.activities.trash.features.search

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
import com.example.cases.activities.home.features.add.AddCase
import com.example.cases.activities.home.features.search.SearchCase
import com.example.cases.activities.trash.features.view.ViewTrashCase
import com.example.cases.adapter.home.CasesAdapter
import com.example.cases.adapter.trash.TrashAdapter
import com.example.cases.database.db.CaseDatabase
import com.example.cases.databinding.ActivitySearchCaseBinding
import com.example.cases.databinding.ActivitySearchTrashCaseBinding
import com.example.cases.models.data.home.Case
import com.example.cases.models.data.trash.Trash
import com.example.cases.models.vm.home.CaseViewModel
import com.example.cases.models.vm.trash.TrashViewModel
import java.util.*

class SearchTrashCase : AppCompatActivity(), TrashAdapter.CasesClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivitySearchTrashCaseBinding
    private lateinit var database: CaseDatabase
    lateinit var viewModel: TrashViewModel
    lateinit var adapter: TrashAdapter
    lateinit var selectedTrash: Trash

    private val updateTrash = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val trash = result.data?.getSerializableExtra("trash") as? Trash

            if (trash != null) {
                viewModel.updateTrash(trash)
            }
        }
    }

    private val getTrashContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val trash = result.data?.getSerializableExtra("trash") as? Trash

                if (trash != null) {
                    viewModel.insertTrash(trash)
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchTrashCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUI()
    }

    private fun initializeUI() {
        binding.trashSearchRecyclerView.setHasFixedSize(true)
        binding.trashSearchRecyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = TrashAdapter(this, this)
        binding.trashSearchRecyclerView.adapter = adapter

        binding.trashSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel = ViewModelProvider(this@SearchTrashCase, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
                        TrashViewModel::class.java)
                    viewModel.allTrash.observe(this@SearchTrashCase) { list ->
                        list?.let {
                            for (item in list) {
                                if (item.title?.lowercase()?.contains(newText.lowercase()) == true || item.databaseCase?.lowercase()?.contains(newText.lowercase()) == true) {
                                    database = CaseDatabase.getDatabase(this@SearchTrashCase)
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

    override fun onItemClicked(trash: Trash) {
        val intent = Intent(this@SearchTrashCase, ViewTrashCase::class.java)
        intent.putExtra("current_trash", trash)
        updateTrash.launch(intent)
    }

    override fun onLongItemClicked(trash: Trash, cardView: CardView) {
        selectedTrash = trash
        popupDisplay(cardView)
    }

    private fun popupDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@SearchTrashCase)
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