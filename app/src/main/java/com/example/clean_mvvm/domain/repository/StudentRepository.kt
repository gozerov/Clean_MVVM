package com.example.clean_mvvm.domain.repository

import com.example.clean_mvvm.domain.entity.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepository : Repository {

    suspend fun getStudentById(id: Long): Student

    suspend fun setCurrentStudent(student: Student) : Flow<Int>

    suspend fun renameStudent(student: Student) : Flow<Int>

    suspend fun listenStudents() : Flow<List<Student>>

    suspend fun updateStudent(student: Student)

}