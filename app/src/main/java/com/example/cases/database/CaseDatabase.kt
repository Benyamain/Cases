package com.example.cases.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cases.models.Case
import com.example.cases.utilities.DATABASE_NAME

@Database(entities = arrayOf(Case::class), version = 1, exportSchema = false)
abstract class CaseDatabase: RoomDatabase() {

    abstract fun getCaseDao(): CaseDao

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