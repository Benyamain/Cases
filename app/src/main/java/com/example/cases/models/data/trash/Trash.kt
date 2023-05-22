package com.example.cases.models.data.trash

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cases.activities.common.Note

@Entity(tableName = "trash_table")
data class Trash(
    @PrimaryKey(autoGenerate = true) override val id: Int?,
    @ColumnInfo(name = "title") override val title: String?,
    @ColumnInfo(name = "database_case") override val databaseCase: String?,
    @ColumnInfo(name = "date") override val date: String?
) : Note {
    override fun toString(): String {
        return "$title\nDate: $date\nDetails: $databaseCase\n\n\n"
    }
}
