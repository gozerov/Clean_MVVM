package com.example.clean_mvvm.presentation.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.clean_mvvm.R
import com.example.clean_mvvm.application.APP_PREFERENCES
import com.example.clean_mvvm.application.appComponent
import com.example.clean_mvvm.core.views.BaseFragment
import com.example.clean_mvvm.core.views.HasCustomBar
import com.example.clean_mvvm.databinding.FragmentUserItemBinding
import com.example.clean_mvvm.domain.entity.student.Student
import com.example.clean_mvvm.presentation.viewmodels.StudentViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class StudentFragment: BaseFragment(R.layout.fragment_user_item), HasCustomBar {

    private lateinit var binding: FragmentUserItemBinding

    private val args: StudentFragmentArgs by navArgs()

    override val viewModel: StudentViewModel by viewModels {
        factory.create(args.student.id)
    }

    @Inject
    lateinit var factory: StudentViewModel.StudentFactory.Factory

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserItemBinding.inflate(inflater, container, false)
        collectFlow(viewModel.viewState) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onSuccess = { viewState ->
                    val student = viewState.student
                    with(binding) {

                        renameButton.visibility = if (viewState.showRenameButton)
                            View.VISIBLE
                        else
                            View.INVISIBLE

                        if (student.isRenamed) {
                            renameButton.isEnabled = false
                            renameButton.setBackgroundColor(ResourcesCompat.getColor(
                                resources,
                                R.color.gray,
                                requireActivity().theme)
                            )
                        }

                        renameProgressHorizontalGroup.visibility = if (viewState.showProgressBar)
                            View.VISIBLE
                        else
                            View.INVISIBLE

                        linearProgressBar.progress = viewState.renameProgressPercentage
                        progressNumTextView.text = viewState.renameProgressPercentageMessage

                        usernameTextView.text = student.fullName
                        schoolTextView.text = student.school
                        classNumTextView.text = student.schoolClass
                    }
                }
            )
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.renameEvent.collect { student ->
                parentFragmentManager.findFragmentById(R.id.fragmentContainer)?.arguments?.putSerializable(
                    ARG_STUDENT, student
                )
                viewModel.updateToolbar(student.fullName)
            }
        }

        binding.renameButton.setOnClickListener {
            viewModel.renameStudent()
        }

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }
        return binding.root
    }

    override fun onPause() {
        val prefs = context?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            ?: throw NullPointerException()
        prefs
            .edit()
            .putLong(MenuFragment.STUDENT_ID, getStudent().id)
            .apply()
        super.onPause()
    }

    private fun getStudent(): Student {
        return arguments?.getSerializable(ARG_STUDENT) as Student
    }

    override fun getCustomTitle(): String = viewModel.currentStudent?.fullName ?: ""

    companion object {
        const val ARG_STUDENT = "student"
    }
}