package com.example.clean_mvvm.domain.entity.student

import java.io.Serializable

data class Student(
    val id: Long,
    val fullName: String,
    val school: String,
    val schoolClass: String,
    val isRenamed: Boolean = false
    ): Serializable