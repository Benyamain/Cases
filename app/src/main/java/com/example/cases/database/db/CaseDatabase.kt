package com.example.cases.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cases.database.home.dao.CaseDao
import com.example.cases.database.trash.dao.TrashDao
import com.example.cases.models.data.home.Case
import com.example.cases.models.data.trash.Trash
import com.example.cases.utilities.DATABASE_NAME

@Database(entities = [Case::class, Trash::class], version = 4, exportSchema = false)
abstract class CaseDatabase: RoomDatabase() {

    abstract fun getCaseDao(): CaseDao
    abstract fun getTrashDao(): TrashDao

    companion object {
        @Volatile
        private var INSTANCE: CaseDatabase? = null

        fun getDatabase(context: Context): CaseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CaseDatabase::class.java,
                    DATABASE_NAME).build()

                INSTANCE = instance
                instance
            }
        }
    }
}