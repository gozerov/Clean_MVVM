package foundation.dagger

import dagger.Module
import dagger.Provides
import foundation.model.coroutines.IoDispatcher
import kotlinx.coroutines.Dispatchers


@Module(includes = [AppBindModule::class, RoomModule::class])
class AppModule {

    @Provides
    fun provideIoDispatcher() = IoDispatcher(Dispatchers.IO)

}