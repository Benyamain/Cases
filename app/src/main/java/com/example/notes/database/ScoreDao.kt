package com.example.notes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notes.models.Score

@Dao
interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(score: Score)

    @Delete
    suspend fun delete(score: Score)

    @Query("SELECT * FROM scores_table ORDER BY id ASC")
    fun getAllScores(): LiveData<List<Score>>

    @Query("UPDATE scores_table SET title = :title, score = :score WHERE id = :id")
    suspend fun update(id: Int?, title: String?, score: String?)
}