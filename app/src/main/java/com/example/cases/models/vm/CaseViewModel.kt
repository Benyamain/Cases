package com.example.cases.models.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cases.database.db.CaseDatabase
import com.example.cases.database.home.repository.CasesRepository
import com.example.cases.models.data.home.Case
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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