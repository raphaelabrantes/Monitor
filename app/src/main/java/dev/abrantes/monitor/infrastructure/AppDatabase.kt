package dev.abrantes.monitor.infrastructure

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Response::class, RegisterUrl::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun responseDao(): ResponseDao

    abstract fun registerUrlDao(): RegisterUrlDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "database"
                )
                    .createFromAsset("database/database.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

