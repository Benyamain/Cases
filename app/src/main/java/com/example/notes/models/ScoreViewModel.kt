package com.example.notes.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.notes.database.ScoreDatabase
import com.example.notes.database.ScoresRepository

class ScoreViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ScoresRepository
    val allScores: LiveData<List<Score>>

    init {
        val dao = ScoreDatabase.getDatabase(application).getScoreDao()
        repository = ScoresRepository(dao)
        allScores = repository.alLScores
    }


}