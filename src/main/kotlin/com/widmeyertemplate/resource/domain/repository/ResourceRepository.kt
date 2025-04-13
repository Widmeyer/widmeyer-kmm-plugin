package com.widmeyertemplate.resource.domain.repository

import com.intellij.openapi.project.Project
import com.widmeyertemplate.utils.Result
import kotlinx.coroutines.flow.Flow

interface ResourceRepository {
    fun update(project: Project): Flow<Result<String, String>>
}