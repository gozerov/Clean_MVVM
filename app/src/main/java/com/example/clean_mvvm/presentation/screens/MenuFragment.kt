package com.example.clean_mvvm.presentation.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clean_mvvm.R
import com.example.clean_mvvm.databinding.FragmentRecyclerviewBinding
import com.example.clean_mvvm.presentation.adapter.BaseAdapter
import foundation.views.BaseFragment
import com.example.clean_mvvm.presentation.viewmodels.MenuViewModel
import com.example.clean_mvvm.presentation.viewmodels.MenuViewModel.MenuEvents.*
import foundation.views.APP_PREFERENCES
import foundation.views.appComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuFragment: BaseFragment(R.layout.fragment_recyclerview) {

    private lateinit var adapter: BaseAdapter
    private lateinit var binding: FragmentRecyclerviewBinding

    override val viewModel: MenuViewModel by viewModels { factory }

    @Inject
    lateinit var factory: MenuViewModel.StudentFactory

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecyclerviewBinding.inflate(inflater, container, false)
        adapter = BaseAdapter()

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }

        adapter.elementLiveData.observe(viewLifecycleOwner) {
            it.getValue()?.let { student -> viewModel.onElementPressed(student) }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.students.collect { result ->
                    renderSimpleResult(binding.root, result) {
                        adapter.students = it
                        initSharedPrefs()
                    }
                }
            }
            launch {
                viewModel.eventFlow.collect { event ->
                    when (event) {
                        is NavigateToStudentScreen -> {
                            val action = MenuFragmentDirections.actionMenuFragment2ToStudentFragment(event.student)
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

    private fun initSharedPrefs() {
        viewLifecycleOwner.lifecycleScope.launch {
            val prefs = context?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE) ?: throw NullPointerException()
            val fullName = viewModel.getStudentById(prefs.getLong(STUDENT_ID, 1)).fullName
            binding.currentStudentView.text = getString(R.string.current_student, fullName)
        }

    }

    override fun onResume() {
        super.onResume()
        binding.recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.adapter = adapter
    }

    companion object {
        const val STUDENT_ID = "STUDENT_ID"
    }
}