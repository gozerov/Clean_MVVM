package com.example.clean_mvvm.data.cache.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.clean_mvvm.data.cache.entity.DBStudent

@Database(entities = [DBStudent::class], version = 1)
abstract class StudentsDatabase : RoomDatabase() {

    abstract fun getStudentDao() : StudentDao

    companion object {
        private var database: StudentsDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : StudentsDatabase {
            return database ?: Room.databaseBuilder(context, StudentsDatabase::class.java, "db").build()
        }
    }
}