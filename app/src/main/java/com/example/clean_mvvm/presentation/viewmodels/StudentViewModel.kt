package com.example.clean_mvvm.presentation.viewmodels

import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import com.example.clean_mvvm.R
import com.example.clean_mvvm.domain.entity.Student
import com.example.clean_mvvm.domain.repository.StudentRepository
import foundation.views.BaseViewModel
import com.example.clean_mvvm.presentation.screens.StudentFragment.*
import foundation.model.*
import kotlinx.coroutines.launch
import com.example.clean_mvvm.domain.entity.Result
import com.example.clean_mvvm.domain.entity.SuccessResult
import com.example.clean_mvvm.domain.entity.takeSuccess
import foundation.utils.finiteShareIn
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import com.example.clean_mvvm.presentation.screens.StudentFragment.Companion.ARG_STUDENT

class StudentViewModel (
    private val studentService: StudentRepository,
    private val savedStateHandle: SavedStateHandle
): BaseViewModel() {

    private var _currentStudent = MutableStateFlow(
        SuccessResult(
        savedStateHandle.get<Student>(ARG_STUDENT)!!)
    )
        set(value) {
            field = value
            savedStateHandle.set(ARG_STUDENT, value.value.data)
        }

    private val _instanceRenameInProgress = MutableStateFlow<Progress>(EmptyProgress)
    private val _sampledRenameInProgress = MutableStateFlow<Progress>(EmptyProgress)

    var currentStudent: Student? = null

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

            val flow = studentService.renameStudent(_currentStudent.value.takeSuccess()!!)
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

            _currentStudent.value = SuccessResult(studentService.getStudentById(_currentStudent.value.data.id))
            toast(R.string.user_has_been_renamed)
            _renameEvent.emit(_currentStudent.value.data)
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

    private fun load() = viewModelScope.launch {
        studentService.setCurrentStudent(_currentStudent.value.data).collect()
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
}
