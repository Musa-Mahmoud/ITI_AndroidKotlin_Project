package com.iti.forecozy.utils.types

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    fun isSuccess() = this is Success<*> && data != null
    fun isError() = this is Error
    fun isLoading() = this is Loading
}