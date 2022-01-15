package com.example.clean_mvvm.dagger

import android.content.Context
import com.example.clean_mvvm.application.App
import com.example.clean_mvvm.presentation.screens.MenuFragment
import com.example.clean_mvvm.presentation.screens.StudentFragment
import com.example.clean_mvvm.presentation.viewmodels.MenuViewModel
import com.example.clean_mvvm.presentation.viewmodels.StudentViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(viewModel: StudentViewModel)
    fun inject(viewModel: MenuViewModel)
    fun inject(app: App)
    fun inject(fragment: StudentFragment)
    fun inject(fragment: MenuFragment)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun context(context: Context): Builder

    }

}
