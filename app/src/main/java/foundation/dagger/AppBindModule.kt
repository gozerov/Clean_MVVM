package foundation.dagger

import dagger.Binds
import dagger.Module
import com.example.clean_mvvm.domain.repository.StudentRepository
import com.example.clean_mvvm.data.repository.StudentRealization
import javax.inject.Singleton

@Module
interface AppBindModule {

    @Suppress("unused")
    @Singleton
    @Binds
    fun bindStudentImplToStudentRepository(studentRealization: StudentRealization): StudentRepository

}