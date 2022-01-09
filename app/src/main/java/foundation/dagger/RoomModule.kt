package foundation.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import foundation.model.sqlite.StudentsDatabase

@Module
class RoomModule {

    @Provides
    fun provideDao(context: Context) = StudentsDatabase.getInstance(context).getStudentDao()

}