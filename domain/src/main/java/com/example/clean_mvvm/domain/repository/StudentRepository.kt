package com.example.clean_mvvm.domain.repository

import com.example.clean_mvvm.domain.entity.student.Student
import com.example.clean_mvvm.domain.entity.student.StudentId
import kotlinx.coroutines.flow.Flow

interface StudentRepository : Repository {

    suspend fun getStudentById(studentId: StudentId): Student

    suspend fun renameStudent(studentId: StudentId)

    suspend fun listenStudents() : Flow<List<Student>>

    suspend fun getStudentsCount(): Int

    suspend fun firstInitialization()

}