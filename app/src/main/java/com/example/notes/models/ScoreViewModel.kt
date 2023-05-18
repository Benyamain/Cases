package com.example.notes.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.database.ScoreDatabase
import com.example.notes.database.ScoresRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScoreViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ScoresRepository
    val allScores: LiveData<List<Score>>

    init {
        val dao = ScoreDatabase.getDatabase(application).getScoreDao()
        repository = ScoresRepository(dao)
        allScores = repository.alLScores
    }

    fun deleteScore(score: Score) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(score)
    }

    fun insertScore(score: Score) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(score)
    }

    fun updateScore(score: Score) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(score)
    }
}