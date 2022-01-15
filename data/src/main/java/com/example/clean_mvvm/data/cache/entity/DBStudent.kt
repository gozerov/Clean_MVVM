package com.example.clean_mvvm.data.cache.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = AppSQLiteContract.StudentsTable.TABLE_NAME)
data class DBStudent(
    @PrimaryKey @ColumnInfo(name = AppSQLiteContract.StudentsTable.COLUMN_ID)
    val id: Long,
    @ColumnInfo(name = AppSQLiteContract.StudentsTable.COLUMN_FULLNAME)
    val fullName: String,

    @ColumnInfo(name = AppSQLiteContract.StudentsTable.COLUMN_SCHOOL)
    val school: String,

    @ColumnInfo(name = AppSQLiteContract.StudentsTable.COLUMN_SCHOOL_GRADE)
    val schoolClass: String,

    @ColumnInfo(name = AppSQLiteContract.StudentsTable.COLUMN_IS_RENAMED)
    val isRenamed: Boolean = false
)