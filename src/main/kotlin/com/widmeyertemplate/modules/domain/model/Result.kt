package com.widmeyertemplate.modules.domain.model

sealed interface Result<out T, out Y> {
    data class Success<T>(val data: T) : Result<T, Nothing>
    data class Failure<Y>(val error: Y) : Result<Nothing, Y>
}
