package com.example.clean_mvvm.data.repository

import com.example.clean_mvvm.data.cache.room.RoomStudentRepository
import com.example.clean_mvvm.data.cache.entity.DataStudent
import com.example.clean_mvvm.data.cache.entity.DataStudentId
import com.example.clean_mvvm.domain.entity.student.Student
import com.example.clean_mvvm.domain.entity.student.StudentId
import com.example.clean_mvvm.domain.repository.StudentRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class StudentRepositoryImpl @Inject constructor(
    private val roomStudentRepository: RoomStudentRepository
) : StudentRepository {

    override suspend fun getStudentById(studentId: StudentId): Student {
        return roomStudentRepository.getStudentById(studentId.toDataStudentId()).toStudent()
    }

    override suspend fun renameStudent(studentId: StudentId) {
        return roomStudentRepository.renameStudent(studentId.toDataStudentId())
    }

    override suspend fun listenStudents(): Flow<List<Student>> {
        return roomStudentRepository.listenStudents().toFlowListStudent()
    }

    override suspend fun getStudentsCount(): Int = roomStudentRepository.getStudentsCount()

    override suspend fun firstInitialization() = roomStudentRepository.firstInitialization()

    private fun DataStudent.toStudent() = Student(id, fullName, school, schoolClass, isRenamed)
    private fun StudentId.toDataStudentId() = DataStudentId(id)
    private fun List<DataStudent>.toListStudent() = this.map { it.toStudent() }
    private fun Flow<List<DataStudent>>.toFlowListStudent() = this.map { it.toListStudent() }

}