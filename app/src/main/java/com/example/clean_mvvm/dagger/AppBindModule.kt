package com.example.clean_mvvm.dagger

import com.example.clean_mvvm.data.cache.room.RoomStudentRepository
import com.example.clean_mvvm.data.cache.room.RoomStudentRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton
import com.example.clean_mvvm.data.repository.StudentRepositoryImpl
import com.example.clean_mvvm.domain.repository.StudentRepository

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