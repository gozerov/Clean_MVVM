package foundation.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import com.example.clean_mvvm.data.cache.room.StudentsDatabase

@Module
class RoomModule {

    @Provides
    fun provideDao(context: Context) = StudentsDatabase.getInstance(context).getStudentDao()

}