package com.example.cases.database

import androidx.lifecycle.LiveData
import com.example.cases.models.Case

// Acts as an intermediate between our activities and our database
class CasesRepository(private val caseDao: CaseDao) {

    val allCases: LiveData<List<Case>> = caseDao.getAllCases()

    suspend fun insert(case: Case) {
        caseDao.insert(case)
    }

    suspend fun delete(case: Case) {
        caseDao.delete(case)
    }

    suspend fun update(case: Case) {
        caseDao.update(case.id, case.title, case.databaseCase)
    }
}