package com.example.notes.database

import androidx.lifecycle.LiveData
import com.example.notes.models.Score

// Acts as an intermediate between our activities and our database
class ScoresRepository(private val scoreDao: ScoreDao) {

    val alLScores: LiveData<List<Score>> = scoreDao.getAllScores()

    suspend fun insert(score: Score) {
        scoreDao.insert(score)
    }

    suspend fun delete(score: Score) {
        scoreDao.delete(score)
    }

    suspend fun update(score: Score) {
        scoreDao.update(score.id, score.title, score.score)
    }
}