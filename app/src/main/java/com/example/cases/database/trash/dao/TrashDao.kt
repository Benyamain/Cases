package com.example.cases.database.trash.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cases.models.data.trash.Trash

@Dao
interface TrashDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trash: Trash)

    @Delete
    suspend fun deleteTrash(trash: Trash)

    @Query("SELECT * FROM trash_table ORDER BY id ASC")
    fun getAllTrashCases(): LiveData<List<Trash>>

    @Query("UPDATE trash_table SET title = :title, database_case = :databaseCase WHERE id = :id")
    suspend fun update(id: Int?, title: String?, databaseCase: String?)
}