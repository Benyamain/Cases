package com.example.cases.models.vm.home

import android.app.Application
import android.net.Uri
import android.os.Environment
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cases.database.db.CaseDatabase
import com.example.cases.database.home.repository.CasesRepository
import com.example.cases.models.data.home.Case
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class CaseViewModel(application: Application): AndroidViewModel(application) {
    private val repository: CasesRepository
    val allCases: LiveData<List<Case>>

    init {
        val dao = CaseDatabase.getDatabase(application).getCaseDao()
        repository = CasesRepository(dao)
        allCases = repository.allCases
    }

    fun deleteCase(case: Case) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(case)
    }

    fun insertCase(case: Case) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(case)
    }

    fun updateCase(case: Case) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(case)
    }
}