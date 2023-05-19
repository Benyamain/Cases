package com.example.cases.models.vm.trash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cases.database.db.CaseDatabase
import com.example.cases.database.trash.repository.TrashRepository
import com.example.cases.models.data.trash.Trash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrashViewModel(application: Application): AndroidViewModel(application) {
    private val trashRepository: TrashRepository
    val allTrash: LiveData<List<Trash>>

    init {
        val trashDao = CaseDatabase.getDatabase(application).getTrashDao()
        trashRepository = TrashRepository(trashDao)
        allTrash = trashRepository.allTrash
    }

    fun deleteTrash(trash: Trash) = viewModelScope.launch(Dispatchers.IO) {
        trashRepository.deleteTrash(trash)
    }

    fun insertTrash(trash: Trash) = viewModelScope.launch(Dispatchers.IO) {
        trashRepository.insertTrash(trash)
    }

    fun updateTrash(trash: Trash) = viewModelScope.launch(Dispatchers.IO) {
        trashRepository.updateTrash(trash)
    }
}