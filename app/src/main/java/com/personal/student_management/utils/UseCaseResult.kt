package com.personal.student_management.utils

sealed class UseCaseResult<out T> {
    data class Success<out T>(val data: T) : UseCaseResult<T>()
    data class Error(val message: String) : UseCaseResult<Nothing>()
}

