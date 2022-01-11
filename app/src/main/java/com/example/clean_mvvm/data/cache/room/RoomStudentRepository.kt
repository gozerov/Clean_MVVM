package com.example.clean_mvvm.data.cache.room

import com.example.clean_mvvm.data.cache.entity.DataStudent
import com.example.clean_mvvm.data.cache.entity.DataStudentId
import kotlinx.coroutines.flow.Flow

interface RoomStudentRepository {

    suspend fun getStudentById(studentId: DataStudentId): DataStudent

    suspend fun renameStudent(studentId: DataStudentId)

    suspend fun listenStudents(): Flow<List<DataStudent>>

    suspend fun getStudentsCount(): Int

    suspend fun firstInitialization()

}