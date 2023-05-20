package com.example.cases.models.data.trash

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trash_table")
data class Trash(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "database_case") val databaseCase: String?,
    @ColumnInfo(name = "date") val date: String?
) : java.io.Serializable {
    override fun toString(): String {
        return "$title\nDate: $date\nDetails: $databaseCase\n\n\n"
    }
}
