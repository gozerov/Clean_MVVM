package foundation.views

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.clean_mvvm.application.App
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import foundation.dagger.AppComponent
import com.example.clean_mvvm.domain.repository.StudentRepository
import java.lang.reflect.Constructor

const val APP_PREFERENCES = "APP_SHARED_PREFERENCES"

class CustomViewModelFactory @AssistedInject constructor(
    private val studentService: StudentRepository,
    @Assisted("owner") private val owner: SavedStateRegistryOwner,
    @Assisted("defaultArgs") private val defaultArgs: Bundle?
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val constructors = modelClass.constructors
        val constructor = constructors.maxByOrNull { it.typeParameters.size }!!
        val dependencies: List<Any?> = mutableListOf(studentService, owner, defaultArgs)
        val dependenciesWithSavedState = (dependencies + handle).filterIsInstance<Any>()

        val arguments = findDependencies(constructor, dependenciesWithSavedState)
        return constructor.newInstance(*arguments.toTypedArray()) as T
    }

    private fun findDependencies(constructor: Constructor<*>, dependencies: List<Any>): List<Any> {
        val args = mutableListOf<Any>()
        constructor.parameterTypes.forEach { parameterClass ->
            val dependency = dependencies.first { parameterClass.isAssignableFrom(it.javaClass) }
            args.add(dependency)
        }
        return args
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("owner") owner: SavedStateRegistryOwner,
            @Assisted("defaultArgs") defaultArgs: Bundle? = null
        ): CustomViewModelFactory
    }
}

/*inline fun <reified VM: ViewModel> BaseFragment.customFactory(
    factory: CustomViewModelFactory.Factory,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null): Lazy<VM> {
    return viewModels { factory.create(owner, defaultArgs) }
}*/

val Context.appComponent: AppComponent
    get() = when(this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
}