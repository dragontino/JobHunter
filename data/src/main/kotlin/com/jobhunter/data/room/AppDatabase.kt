package com.jobhunter.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jobhunter.data.room.model.VacancyEntity

@Database(
    entities = [VacancyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vacanciesDao(): LikedVacanciesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tmp = INSTANCE
            if (tmp != null) {
                return tmp
            }

            synchronized(this) {
                val instance = Room
                    .databaseBuilder(context, AppDatabase::class.java, "Vacancies.db")
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}