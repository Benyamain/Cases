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

    @Query("UPDATE trash_table SET title = :title, database_case = :databaseCase, slider_one = :sliderOne, slider_two = :sliderTwo, slider_three = :sliderThree" +
            ", slider_four = :sliderFour, slider_five = :sliderFive, slider_six = :sliderSix, slider_seven = :sliderSeven, slider_eight = :sliderEight WHERE id = :id")
    suspend fun update(id: Int?, title: String?, databaseCase: String?, sliderOne: String?, sliderTwo: String?, sliderThree: String?,
                       sliderFour: String?, sliderFive: String?, sliderSix: String?, sliderSeven: String?, sliderEight: String?)
}