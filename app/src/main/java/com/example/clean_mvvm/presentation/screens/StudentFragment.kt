package com.example.clean_mvvm.presentation.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.clean_mvvm.R
import com.example.clean_mvvm.databinding.FragmentUserItemBinding
import com.example.clean_mvvm.domain.entity.Student
import com.example.clean_mvvm.presentation.viewmodels.StudentViewModel
import foundation.views.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class StudentFragment: BaseFragment(R.layout.fragment_user_item), HasCustomBar {

    private lateinit var binding: FragmentUserItemBinding

    private val args: StudentFragmentArgs by navArgs()

    override val viewModel: StudentViewModel by viewModels {
        factory.create(this, bundleOf(ARG_STUDENT to args.student))
    }

    @Inject
    lateinit var factory: CustomViewModelFactory.Factory

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
                parentFragmentManager.findFragmentById(R.id.fragmentContainer)?.arguments?.putParcelable(
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
        return arguments?.getParcelable(ARG_STUDENT) ?: throw IllegalStateException()
    }

    override fun getCustomTitle(): String = getStudent().fullName

    companion object {
        const val ARG_STUDENT = "student"
    }

}