package com.example.clean_mvvm.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clean_mvvm.core.model.result.PendingResult
import com.example.clean_mvvm.core.model.result.Result
import com.example.clean_mvvm.core.model.result.SuccessResult
import com.example.clean_mvvm.domain.entity.student.Student
import com.example.clean_mvvm.domain.entity.student.StudentId
import com.example.clean_mvvm.domain.usecase.GetCurrentStudentUseCase
import com.example.clean_mvvm.domain.usecase.ListenStudentsUseCase
import com.example.clean_mvvm.core.views.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuViewModel(
    private val listenStudentsUseCase: ListenStudentsUseCase,
    private val getCurrentStudentUseCase: GetCurrentStudentUseCase
): BaseViewModel() {

    private val _eventFlow = MutableSharedFlow<MenuEvents>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val eventFlow = _eventFlow.asSharedFlow()

    private var _students = MutableStateFlow<Result<List<Student>>>(PendingResult())
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

    suspend fun getStudentById(id: Long): Student = getCurrentStudentUseCase.execute(StudentId(id))


    private fun listenRepository() {
        viewModelScope.launch {
            listenStudentsUseCase.execute().collect {
                _students.value = SuccessResult(it)
            }
        }
    }

    sealed class MenuEvents {

        data class NavigateToStudentScreen(val student: Student) : MenuEvents()

    }

    class StudentFactory @Inject constructor(
        private val listenStudentsUseCase: ListenStudentsUseCase,
        private val getCurrentStudentUseCase: GetCurrentStudentUseCase
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MenuViewModel(listenStudentsUseCase, getCurrentStudentUseCase) as T
        }
    }

}