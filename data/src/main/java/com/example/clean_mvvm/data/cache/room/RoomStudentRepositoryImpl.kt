package com.example.clean_mvvm.data.cache.room

import com.example.clean_mvvm.data.cache.entity.DataStudent
import com.example.clean_mvvm.data.cache.entity.DataStudentId
import com.example.clean_mvvm.data.cache.entity.DBStudent
import com.example.clean_mvvm.data.cache.entity.IoDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomStudentRepositoryImpl @Inject constructor(
    private val ioDispatcher: IoDispatcher,
    private val studentDao: StudentDao
): RoomStudentRepository {
    override suspend fun getStudentById(studentId: DataStudentId): DataStudent = withContext(ioDispatcher.value) {
        return@withContext studentDao.getStudentById(studentId.id).toDataStudent()
    }

    override suspend fun renameStudent(studentId: DataStudentId) = withContext(ioDispatcher.value) {
        return@withContext studentDao.updateStudentUserName("Renamed User", studentId.id)
    }

    override suspend fun listenStudents(): Flow<List<DataStudent>> = withContext(ioDispatcher.value) {
        return@withContext studentDao.getAllStudents().toFlowDataStudentList()
    }

    override suspend fun getStudentsCount(): Int = studentDao.getStudentsCount()

    override suspend fun firstInitialization() = withContext(ioDispatcher.value) {
        val students = (1..50).map {
            DataStudent(
                id = it.toLong(),
                fullName = "Ученик $it",
                school = "Лицей №8",
                schoolClass = "10 А"
            )
        }
        studentDao.firstInitialization(students.toDBStudentList())
    }

    private fun DBStudent.toDataStudent() = DataStudent(id, fullName, school, schoolClass, isRenamed)
    private fun DataStudent.toDBStudent() = DBStudent(id, fullName, school, schoolClass, isRenamed)
    private fun Flow<List<DBStudent>>.toFlowDataStudentList() = this.map { it.map { dbStudent -> dbStudent.toDataStudent() } }
    private fun List<DataStudent>.toDBStudentList() = this.map { it.toDBStudent() }

}