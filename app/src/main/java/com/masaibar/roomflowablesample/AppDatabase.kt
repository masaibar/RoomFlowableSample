package com.masaibar.roomflowablesample

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [Time::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        fun create(context: Context): AppDatabase {
            val databaseBuilder =
                    Room.databaseBuilder(context, AppDatabase::class.java, "app.db")

            return databaseBuilder
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }

    abstract fun getTimeDao(): TimeDao

}