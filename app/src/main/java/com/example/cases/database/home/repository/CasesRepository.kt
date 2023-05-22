package com.example.cases.database.home.repository

import androidx.lifecycle.LiveData
import com.example.cases.database.home.dao.CaseDao
import com.example.cases.models.data.home.Case

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
        caseDao.update(case.id, case.title, case.databaseCase, case.sliderOne, case.sliderTwo, case.sliderThree, case.sliderFour,
            case.sliderFive, case.sliderSix, case.sliderSeven, case.sliderEight)
    }
}