package com.example.clean_mvvm.domain.usecase

import com.example.clean_mvvm.domain.entity.Student
import com.example.clean_mvvm.domain.repository.StudentRepository
import javax.inject.Inject

class GetCurrentStudentUseCase @Inject constructor(private val repository: StudentRepository) {

    suspend fun execute(studentId: Long): Student {
        return repository.getStudentById(studentId)
    }

}