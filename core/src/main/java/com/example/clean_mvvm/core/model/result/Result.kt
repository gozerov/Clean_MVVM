package com.example.clean_mvvm.core.model.result

import java.io.Serializable
import java.lang.Exception

typealias Mapper<Input, Output> = (Input) -> Output

sealed class Result<T> : Serializable{

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

class PendingResult<T> : Result<T>()

class SuccessResult<T>(
    val data: T
) : FinalResult<T>(), Serializable

class ErrorResult<T>(
    val exception: Exception
) : FinalResult<T>(), Serializable

fun <T> Result<T>.takeSuccess() :T? {
    return if (this is SuccessResult)
        this.data
    else
        null
}