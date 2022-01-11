package com.example.clean_mvvm.presentation.viewmodels

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clean_mvvm.R
import com.example.clean_mvvm.domain.entity.*
import com.example.clean_mvvm.domain.entity.student.Student
import com.example.clean_mvvm.domain.entity.student.StudentId
import foundation.views.BaseViewModel
import com.example.clean_mvvm.presentation.screens.StudentFragment.*
import foundation.model.*
import com.example.clean_mvvm.domain.usecase.GetCurrentStudentUseCase
import com.example.clean_mvvm.domain.usecase.RenameStudentUseCase
import kotlinx.coroutines.flow.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import foundation.utils.finiteShareIn
import kotlinx.coroutines.*

class StudentViewModel (
    private val studentId: Long,
    private val getCurrentStudentUseCase: GetCurrentStudentUseCase,
    private val renameStudentUseCase: RenameStudentUseCase
): BaseViewModel() {

    private var _currentStudent = MutableStateFlow<Result<Student>>(PendingResult())
    var currentStudent: Student? = null

    private val _instanceRenameInProgress = MutableStateFlow<Progress>(EmptyProgress)
    private val _sampledRenameInProgress = MutableStateFlow<Progress>(EmptyProgress)

    val viewState: Flow<Result<ViewState>> = combine(
        _currentStudent,
        _instanceRenameInProgress,
        _sampledRenameInProgress,
        ::mergeSources
    )

    private val _renameEvent = MutableSharedFlow<Student>()
    val renameEvent = _renameEvent.asSharedFlow()

    init {
        load()
    }

    fun renameStudent() = viewModelScope.launch {
        try {
            _instanceRenameInProgress.value = PercentageProgress.START
            _sampledRenameInProgress.value = PercentageProgress.START

            val flow = renameStudentUseCase.execute(StudentId(studentId))
                .finiteShareIn(this)

            val instanceJob = async {
                flow.collect { _instanceRenameInProgress.value = PercentageProgress(it) }
            }

            val sampledJob = async {
                flow.sample(100)
                    .collect { _sampledRenameInProgress.value = PercentageProgress(it) }
            }
            instanceJob.await()
            sampledJob.await()

            toast(R.string.user_has_been_renamed)
            _renameEvent.emit(getCurrentStudentUseCaseResult())
        }
        catch (e: Exception) {
            if (e !is CancellationException) {
                toast(R.string.user_has_been_renamed)
            }
        }
        finally {
            _instanceRenameInProgress.value = EmptyProgress
            _sampledRenameInProgress.value = EmptyProgress
        }
    }

    fun tryAgain() {
        load()
    }

    private suspend fun getCurrentStudentUseCaseResult(): Student {
        val result = getCurrentStudentUseCase.execute(StudentId(studentId))
        _currentStudent.value = SuccessResult(result)
        updateToolbar(result.fullName)
        return result
    }

    private fun load() = viewModelScope.launch {
        delay(500)
        getCurrentStudentUseCaseResult()
    }

    @MainThread
    private suspend fun mergeSources(
        currentStudent: Result<Student>,
        instanceRenameInProgress: Progress,
        sampledRenameInProgress: Progress) : Result<ViewState> {
        val text = getString(R.string.percentage_is, sampledRenameInProgress.getPercentage())
        val viewState = currentStudent.map {
            this@StudentViewModel.currentStudent = it
            ViewState(
                student = it,
                showRenameButton = !instanceRenameInProgress.isInProgress(),
                showProgressBar = instanceRenameInProgress.isInProgress(),
                renameProgressPercentage = instanceRenameInProgress.getPercentage(),
                renameProgressPercentageMessage = text
            )
        }
        return viewState
    }

    data class ViewState(
        val student: Student,
        val showRenameButton: Boolean,
        val showProgressBar: Boolean,
        val renameProgressPercentage: Int,
        val renameProgressPercentageMessage: String
    )

    class StudentFactory @AssistedInject constructor(
        private val getCurrentStudentUseCase: GetCurrentStudentUseCase,
        private val renameStudentUseCase: RenameStudentUseCase,
        @Assisted("studentId") private val studentId: Long
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return StudentViewModel(studentId, getCurrentStudentUseCase, renameStudentUseCase) as T
        }

        @AssistedFactory
        interface Factory {

            fun create(@Assisted("studentId") studentId: Long): StudentFactory

        }
    }

}
