package com.example.cases.models.data.trash

import android.provider.ContactsContract
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trash_table")
data class Trash(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "database_case") val databaseCase: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "slider_one") val sliderOne: String?,
    @ColumnInfo(name = "slider_two") val sliderTwo: String?,
    @ColumnInfo(name = "slider_three") val sliderThree: String?,
    @ColumnInfo(name = "slider_four") val sliderFour: String?,
    @ColumnInfo(name = "slider_five") val sliderFive: String?,
    @ColumnInfo(name = "slider_six") val sliderSix: String?,
    @ColumnInfo(name = "slider_seven") val sliderSeven: String?,
    @ColumnInfo(name = "slider_eight") val sliderEight: String?
) : java.io.Serializable {
    override fun toString(): String {
        return "Title: $title\nDate: $date\nSlider One: $sliderOne\tSlider Two: $sliderTwo\tSlider Three: $sliderThree\tSlider Four: $sliderFour" +
                "\nSlider Five: $sliderFive\tSlider Six: $sliderSix\tSlider Seven: $sliderSeven\tSlider Eight: $sliderEight\nDetails: $databaseCase\n\n\n"
    }
}
