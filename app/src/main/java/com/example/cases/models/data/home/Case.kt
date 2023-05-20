package com.example.cases.models.data.home

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cases_table")
data class Case(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "database_case") val databaseCase: String?,
    @ColumnInfo(name = "date") val date: String?
) : java.io.Serializable {
    override fun toString(): String {
        return "$title\nDate: $date\nDetails: $databaseCase\n\n\n"
    }
}
