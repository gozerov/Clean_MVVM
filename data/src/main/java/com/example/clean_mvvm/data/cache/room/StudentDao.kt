package com.example.clean_mvvm.data.cache.room

import androidx.room.*
import com.example.clean_mvvm.data.cache.entity.DBStudent
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Insert
    suspend fun insert(student: DBStudent)

    @Insert
    suspend fun firstInitialization(students: List<DBStudent>)

    @Query("SELECT * FROM students")
    fun getAllStudents(): Flow<List<DBStudent>>

    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudentById(id: Long): DBStudent

    @Update(entity = DBStudent::class)
    suspend fun updateStudent(student: DBStudent)

    @Query("SELECT count(*) FROM students")
    suspend fun getStudentsCount(): Int

    @Query("UPDATE students SET fullname = :fullName, 'is_renamed'=1 WHERE id=:id")
    suspend fun updateStudentUserName(fullName: String, id: Long)

}