package foundation.dagger

import com.example.clean_mvvm.data.cache.room.RoomStudentRepository
import com.example.clean_mvvm.data.cache.room.RoomStudentRepositoryImpl
import dagger.Binds
import dagger.Module
import com.example.clean_mvvm.domain.repository.StudentRepository
import com.example.clean_mvvm.data.repository.StudentRepositoryImpl
import javax.inject.Singleton

@Module
interface AppBindModule {

    @Suppress("unused")
    @Singleton
    @Binds
    fun bindStudentImplToStudentRepository(studentRepositoryImpl: StudentRepositoryImpl): StudentRepository

    @Suppress("unused")
    @Binds
    fun bindRoomStudentRepImplToRoomStudentRep(roomStudentRepositoryImpl: RoomStudentRepositoryImpl): RoomStudentRepository

}