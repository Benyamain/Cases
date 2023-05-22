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

    @Query("UPDATE cases_table SET title = :title, database_case = :databaseCase, slider_one = :sliderOne, slider_two = :sliderTwo, slider_three = :sliderThree" +
            ", slider_four = :sliderFour, slider_five = :sliderFive, slider_six = :sliderSix, slider_seven = :sliderSeven, slider_eight = :sliderEight WHERE id = :id")
    suspend fun update(id: Int?, title: String?, databaseCase: String?, sliderOne: String?, sliderTwo: String?, sliderThree: String?,
                       sliderFour: String?, sliderFive: String?, sliderSix: String?, sliderSeven: String?, sliderEight: String?)
}