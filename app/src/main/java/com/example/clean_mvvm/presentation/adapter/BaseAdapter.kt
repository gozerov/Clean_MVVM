package com.example.clean_mvvm.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.clean_mvvm.databinding.ItemRecyclerviewBinding
import com.example.clean_mvvm.domain.entity.student.Student
import foundation.utils.Event

class BaseAdapter : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>(), View.OnClickListener {

    private val _elementLiveData = MutableLiveData<Event<Student>>()
    val elementLiveData = _elementLiveData

    var students: List<Student> = emptyList()
        set(value) {
            val diffUtilCallback = StudentDiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    class BaseViewHolder(
        val binding: ItemRecyclerviewBinding
        ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerviewBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val student = students[position]
        with(holder.binding) {
            holder.itemView.tag = student
            itemUsername.text = student.fullName
            itemClass.text = student.schoolClass
        }
    }

    override fun getItemCount(): Int = students.size

    override fun onClick(view: View?) {
        val student = view?.tag!! as Student
        _elementLiveData.value = Event(student)
    }
}