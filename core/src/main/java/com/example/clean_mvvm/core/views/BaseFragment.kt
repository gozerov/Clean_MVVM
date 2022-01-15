package com.example.clean_mvvm.core.views

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast.*
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import com.example.clean_mvvm.core.model.result.ErrorResult
import com.example.clean_mvvm.core.model.result.PendingResult
import com.example.clean_mvvm.core.model.result.Result
import com.example.clean_mvvm.core.model.result.SuccessResult
import com.example.clean_mvvm.core.side_effect.resource.Resource
import com.example.clean_mvvm.core.side_effect.toast.Toast
import com.example.clean_mvvm.core.side_effect.toolbar.ToolbarValue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

abstract class BaseFragment(@LayoutRes contentLayoutId: Int): Fragment(contentLayoutId) {
    abstract val viewModel: BaseViewModel

    fun <T> renderResult(root: ViewGroup, result: Result<T>,
                         onPending: () -> Unit,
                         onError: (Exception) -> Unit,
                         onSuccess: (T) -> Unit) {
        root.children.forEach { it.visibility = View.GONE }
        when(result) {
            is PendingResult -> onPending()
            is ErrorResult -> onError(result.exception)
            is SuccessResult -> onSuccess(result.data)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewModelScope.launch {
            viewModel.sideEffectsFlow.collect {
                if (lifecycle.currentState == Lifecycle.State.STARTED || lifecycle.currentState == Lifecycle.State.RESUMED)

                when(it) {

                    is Toast -> makeText(context, it.message, LENGTH_SHORT).show()

                    is Resource -> {
                        if (it.formatArgs!=null)
                            viewModel.resultFlow.tryEmit(getString(it.resId, it.formatArgs))
                        else
                            viewModel.resultFlow.tryEmit(getString(it.resId))
                    }

                    is ToolbarValue -> (requireActivity() as BaseActivity).updateToolbar(it.title)

                }

            }
        }
    }

}