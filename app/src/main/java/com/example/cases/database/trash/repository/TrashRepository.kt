package com.example.cases.database.trash.repository

import androidx.lifecycle.LiveData
import com.example.cases.database.trash.dao.TrashDao
import com.example.cases.models.data.trash.Trash

class TrashRepository(private val trashDao: TrashDao) {

    val allTrash: LiveData<List<Trash>> = trashDao.getAllTrashCases()

    suspend fun insertTrash(trash: Trash) {
        trashDao.insert(trash)
    }

    suspend fun deleteTrash(trash: Trash) {
        trashDao.deleteTrash(trash)
    }

    suspend fun updateTrash(trash: Trash) {
        trashDao.update(trash.id, trash.title, trash.databaseCase, trash.sliderOne, trash.sliderTwo, trash.sliderThree, trash.sliderFour,
            trash.sliderFive, trash.sliderSix, trash.sliderSeven, trash.sliderEight)
    }

}