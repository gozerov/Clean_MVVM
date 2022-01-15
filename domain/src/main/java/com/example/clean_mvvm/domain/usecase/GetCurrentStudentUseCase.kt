package com.example.clean_mvvm.domain.usecase

import com.example.clean_mvvm.domain.entity.student.Student
import com.example.clean_mvvm.domain.entity.student.StudentId
import com.example.clean_mvvm.domain.repository.StudentRepository
import javax.inject.Inject

class GetCurrentStudentUseCase @Inject constructor(
    private val repository: StudentRepository
    ) {

    suspend fun execute(studentId: StudentId): Student {
        return repository.getStudentById(studentId)
    }

}