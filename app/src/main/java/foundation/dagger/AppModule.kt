package foundation.dagger

import dagger.Module
import dagger.Provides
import com.example.clean_mvvm.data.cache.entity.IoDispatcher
import kotlinx.coroutines.Dispatchers


@Module(includes = [AppBindModule::class, RoomModule::class])
class AppModule {

    @Provides
    fun provideIoDispatcher() = IoDispatcher(Dispatchers.IO)

}