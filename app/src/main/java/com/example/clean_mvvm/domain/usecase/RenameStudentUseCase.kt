package com.example.clean_mvvm.domain.usecase

import com.example.clean_mvvm.domain.entity.student.StudentId
import com.example.clean_mvvm.domain.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RenameStudentUseCase @Inject constructor(
    private val studentRepository: StudentRepository
) {

    suspend fun execute(studentId: StudentId): Flow<Int> = withContext(Dispatchers.IO) {
        studentRepository.renameStudent(studentId)
        return@withContext flow {
            var progress = 0
            while (progress < 100) {
                progress += 2
                delay(20)
                emit(progress)
            }
        }
    }

}