package com.example.cases.database.home.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cases.models.data.home.Case

@Dao
interface CaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(case: Case)

    @Delete
    suspend fun delete(case: Case)

    @Query("SELECT * FROM cases_table ORDER BY id ASC")
    fun getAllCases(): LiveData<List<Case>>

    @Query("UPDATE cases_table SET title = :title, database_case = :databaseCase WHERE id = :id")
    suspend fun update(id: Int?, title: String?, databaseCase: String?)
}