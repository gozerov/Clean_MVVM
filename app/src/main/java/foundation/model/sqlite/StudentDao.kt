package foundation.model.sqlite

import androidx.room.*
import com.example.clean_mvvm.domain.entity.Student
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Insert
    suspend fun insert(student: Student)

    @Insert
    suspend fun firstInitialization(students: List<Student>)

    @Query("SELECT * FROM students")
    fun getAllStudents(): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE id = :id")
    fun getStudentById(id: Long): Student

    @Update(entity = Student::class)
    suspend fun updateStudent(student: Student)

    @Query("SELECT count(*) FROM students")
    suspend fun getStudentsCount(): Int

}