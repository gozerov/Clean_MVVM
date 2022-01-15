package com.example.clean_mvvm.dagger

import com.example.clean_mvvm.data.cache.entity.IoDispatcher
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers


@Module(includes = [AppBindModule::class, RoomModule::class])
class AppModule {

    @Provides
    fun provideIoDispatcher() =
        IoDispatcher(Dispatchers.IO)

}