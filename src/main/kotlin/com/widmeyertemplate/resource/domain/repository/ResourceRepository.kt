package com.widmeyertemplate.resource.domain.repository

import com.widmeyertemplate.utils.Result
import kotlinx.coroutines.flow.Flow

interface ResourceRepository {
    fun update(): Flow<Result<Boolean, String>>
}