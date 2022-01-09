package com.example.clean_mvvm.domain.usecase

import com.example.clean_mvvm.domain.entity.Student
import com.example.clean_mvvm.domain.repository.StudentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenStudentsUseCase @Inject constructor(private val repository: StudentRepository) {

    suspend fun execute(): Flow<List<Student>> {
        delay(750)
        return repository.listenStudents()
    }

}