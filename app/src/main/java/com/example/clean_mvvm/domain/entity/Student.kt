package com.example.clean_mvvm.domain.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import foundation.model.sqlite.AppSQLiteContract.StudentsTable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = StudentsTable.TABLE_NAME)
data class Student(
    @PrimaryKey @ColumnInfo(name = StudentsTable.COLUMN_ID)
    val id: Long,
    @ColumnInfo(name = StudentsTable.COLUMN_FULLNAME)
    val fullName: String,

    @ColumnInfo(name = StudentsTable.COLUMN_SCHOOL)
    val school: String,

    @ColumnInfo(name = StudentsTable.COLUMN_SCHOOL_GRADE)
    val schoolClass: String,

    @ColumnInfo(name = StudentsTable.COLUMN_IS_RENAMED)
    val isRenamed: Boolean = false,

): Parcelable