package com.widmeyertemplate.resource.data.repository

import com.intellij.openapi.project.Project
import com.widmeyertemplate.resource.domain.repository.ResourceRepository
import com.widmeyertemplate.utils.Result
import kotlinx.coroutines.flow.Flow

class ResourceRepositoryImpl: ResourceRepository {
    override fun update(project: Project): Flow<Result<String, String>> {
        TODO("Not yet implemented")
    }
}