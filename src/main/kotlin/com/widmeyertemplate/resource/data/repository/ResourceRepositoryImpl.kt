package com.widmeyertemplate.resource.data.repository

import com.intellij.openapi.project.Project
import com.widmeyertemplate.resource.domain.repository.ResourceColorsRepository
import com.widmeyertemplate.resource.domain.repository.ResourceRepository
import com.widmeyertemplate.resource.domain.repository.ResourceStringsRepository
import com.widmeyertemplate.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ResourceRepositoryImpl(project: Project) : ResourceRepository {
    val stringsRepository: ResourceStringsRepository = ResourceStringsRepositoryImpl(project.basePath.orEmpty())
    val colorsRepository: ResourceColorsRepository = ResourceColorsRepositoryImpl(project.basePath.orEmpty())

    override fun update(): Flow<Result<Boolean, String>> = flow {
        try {
            stringsRepository.update()
            colorsRepository.update()
            emit(Result.Success(true))
        } catch (e: Throwable) {
            e.printStackTrace()
            emit(Result.Failure(e.message.orEmpty()))
        }
    }
}