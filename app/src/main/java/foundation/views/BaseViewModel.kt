package foundation.views

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.clean_mvvm.domain.entity.ErrorResult
import com.example.clean_mvvm.domain.entity.PendingResult
import com.example.clean_mvvm.domain.entity.Result
import com.example.clean_mvvm.domain.entity.SuccessResult
import foundation.side_effect.SideEffect
import foundation.side_effect.resource.ResourceImpl
import foundation.side_effect.toast.ToastImpl
import foundation.side_effect.toolbar.ToolbarValueImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

abstract class BaseViewModel: ViewModel() {

    /**
     * ТОТ КТО ПРИДУМАЛ ОТДЕЛЬНЫЙ ЭКСЕПШН ХЭНДЛЕР ДЛЯ КОРУТИН МОЛОДЧИНА!!!
     * ПРОСТО **** ГЕНИЙ
     * ПОЧТИ ОДНА НЕДЕЛЯ НА ПОИСК БАГА
     * ****
     * МУСОР
     * */

    /**
     * ЕСЛИ ХОЧЕШЬ НОРМАЛЬНО ОТРАБОТАТЬ ОШИБКИ НА КОРУТИНАХ, НЕ ЗАБУДЬ ПРО:
     * CoroutineExceptionHandler { _, throwable -> }
     * А ДАЛЬШЕ КАК ПОЖЕЛАЕШЬ ОТРАБАТЫВАЙ ЭТИ ОШИБКИ
     * */


    val resultFlow = MutableStateFlow<Any?>(null)

    private val _sideEffectsFlow = MutableSharedFlow<SideEffect>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val sideEffectsFlow = _sideEffectsFlow.asSharedFlow()

    protected fun toast(@StringRes message: Int) {
        _sideEffectsFlow.tryEmit(ToastImpl(message))
    }

    protected suspend fun getString(@StringRes resId: Int, formatArgs: Any?): String {
        val event = ResourceImpl(resId, formatArgs)
        viewModelScope.launch {
            _sideEffectsFlow.emit(event)
        }.join()

        val value = resultFlow.value
        if (value is String)
            return value
        else
            throw IllegalArgumentException("Failure type")
    }

    fun updateToolbar(title: String) {
        _sideEffectsFlow.tryEmit(ToolbarValueImpl(title))
    }

    private val coroutineContext = SupervisorJob() + Dispatchers.Main.immediate + CoroutineExceptionHandler { _, throwable -> }
    protected val viewModelScope = CoroutineScope(coroutineContext)

    open fun onResult(result: Any?) {}

    override fun onCleared() {
        super.onCleared()
        clearViewModelScope()
    }

    fun <T> into(stateFlow: MutableStateFlow<Result<T>>, block: suspend () -> T) : Job {
        stateFlow.value = PendingResult()
        val job = viewModelScope.launch {
            try {
                stateFlow.value = SuccessResult(block())
            } catch (e: Exception) {
                stateFlow.value = ErrorResult(e)
            }
        }
        return job
    }

    fun <T> SavedStateHandle.getStateFlow(key: String, value: T) : MutableStateFlow<T> {
        val savedStateHandle = this
        val mutableFlow = MutableStateFlow(savedStateHandle[key] ?: value)

        viewModelScope.launch {
            mutableFlow.collect {
                savedStateHandle[key] = it
            }
        }

        viewModelScope.launch {
            savedStateHandle.getLiveData<T>(key).asFlow().collect {
                mutableFlow.value = it
            }
        }
        return mutableFlow
    }

    private fun clearViewModelScope() {
        viewModelScope.cancel()
    }
}