package com.example.clean_mvvm.presentation.viewmodels

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.clean_mvvm.domain.entity.PendingResult
import com.example.clean_mvvm.domain.entity.Result
import com.example.clean_mvvm.domain.entity.Student
import com.example.clean_mvvm.domain.entity.SuccessResult
import com.example.clean_mvvm.domain.usecase.GetCurrentStudentUseCase
import com.example.clean_mvvm.domain.usecase.ListenStudentsUseCase
import foundation.views.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MenuViewModel(
    private val getCurrentStudentUseCase: GetCurrentStudentUseCase,
    private val listenStudentsUseCase: ListenStudentsUseCase,
    savedStateHandle: SavedStateHandle,
): BaseViewModel() {

    private val _eventFlow = MutableSharedFlow<MenuEvents>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val eventFlow = _eventFlow.asSharedFlow()

    private var _students = savedStateHandle.getStateFlow<Result<List<Student>>>(KEY_STUDENTS, PendingResult())
    val students: StateFlow<Result<List<Student>>> = _students.asStateFlow()

    init {
        listenRepository()
    }

    fun onElementPressed(student: Student) = viewModelScope.launch {
        _eventFlow.emit(MenuEvents.NavigateToStudentScreen(student))
    }

    fun tryAgain() {
        listenRepository()
    }

    suspend fun getStudentById(id: Long): Student {
        return getCurrentStudentUseCase.execute(id)
    }

    private fun listenRepository() {
        viewModelScope.launch {
            listenStudentsUseCase.execute().collect {
                _students.value = SuccessResult(it)
            }
        }
    }

    companion object {
        const val KEY_STUDENTS = "students"
    }

    sealed class MenuEvents {

        data class NavigateToStudentScreen(val student: Student) : MenuEvents()

    }

    class StudentFactory @AssistedInject constructor(
        private val listenStudentsUseCase: ListenStudentsUseCase,
        private val getCurrentStudentUseCase: GetCurrentStudentUseCase,
        @Assisted("owner") owner: SavedStateRegistryOwner
    ) : AbstractSavedStateViewModelFactory(owner, null) {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            return MenuViewModel(getCurrentStudentUseCase, listenStudentsUseCase, handle) as T
        }

        @AssistedFactory
        interface Factory {

            fun create(@Assisted("owner") owner: SavedStateRegistryOwner): StudentFactory

        }
    }

}