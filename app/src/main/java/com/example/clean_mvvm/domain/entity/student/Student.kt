package com.example.clean_mvvm.domain.entity.student

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    val id: Long,
    val fullName: String,
    val school: String,
    val schoolClass: String,
    val isRenamed: Boolean = false
    ): Parcelable