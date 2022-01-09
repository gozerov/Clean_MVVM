package com.example.clean_mvvm.data.repository

import com.example.clean_mvvm.domain.entity.Student
import com.example.clean_mvvm.domain.repository.StudentRepository
import foundation.model.coroutines.IoDispatcher
import foundation.model.sqlite.StudentDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StudentRealization @Inject constructor(
    private val ioDispatcher: IoDispatcher,
    private val studentDao: StudentDao
) : StudentRepository {

    private lateinit var currentStudent: Student

    override suspend fun getStudentById(id: Long): Student = withContext(ioDispatcher.value) {
        return@withContext studentDao.getStudentById(id)
    }

    override suspend fun setCurrentStudent(student: Student) : Flow<Int> = flow {
        var progress = 0
        while (progress < 100) {
            progress += 2
            delay(10)
            emit(progress)
        }
        currentStudent = student

    }.flowOn(ioDispatcher.value)

    override suspend fun renameStudent(student: Student) : Flow<Int> = flow {
        if (student is Student) {
            var progress = 0
            while (progress < 100) {
                progress += 2
                delay(20)
                emit(progress)
            }
            val renamedStudent = student.copy(fullName = "Renamed Student", isRenamed = true)
            studentDao.updateStudent(renamedStudent)
            currentStudent = renamedStudent
        } else
            emit(100)
    }

    override suspend fun listenStudents(): Flow<List<Student>> = withContext(ioDispatcher.value) {
        delay(750)
        if (studentDao.getStudentsCount() == 0)
            firstInitialization()
        //currentStudent = studentDao.getCurrentStudent()
        //currentStudentFlow.emit(currentStudent)
        return@withContext studentDao.getAllStudents()
    }

    override suspend fun updateStudent(student: Student) {
        studentDao.updateStudent(student)
    }

    private suspend fun firstInitialization() {
        val students = (1..50).map {
            Student(
                it.toLong(),
                "Ученик $it",
                "Лицей №8",
                "10 А"
            )
        }
        studentDao.firstInitialization(students)
        listenStudents()
    }
}