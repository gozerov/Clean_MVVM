package com.example.clean_mvvm.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.lang.Exception

typealias Mapper<Input, Output> = (Input) -> Output

sealed class Result<T> : Parcelable{

    fun <R> map(mapper: Mapper<T, R>? = null): Result<R> = when(this) {
        is PendingResult -> PendingResult()
        is ErrorResult -> ErrorResult(this.exception)
        is SuccessResult -> {
            if(mapper == null) throw IllegalArgumentException("Error (IAE)")
            SuccessResult(mapper(this.data))
        }
    }

}

sealed class FinalResult<T> : Result<T>()

@Parcelize
class PendingResult<T> : Result<T>()

@Parcelize
class SuccessResult<T>(
    val data: @RawValue T
) : FinalResult<T>()

@Parcelize
class ErrorResult<T>(
    val exception: Exception
) : FinalResult<T>()

fun <T> Result<T>.takeSuccess() :T? {
    return if (this is SuccessResult)
        this.data
    else
        null
}