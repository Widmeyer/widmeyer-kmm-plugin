package com.widmeyertemplate.resource.data.repository

import com.intellij.openapi.project.Project
import com.widmeyertemplate.resource.domain.model.ResourceBuildsFiles
import com.widmeyertemplate.resource.domain.repository.FileRepository
import com.widmeyertemplate.resource.domain.repository.ResourceColorsRepository
import com.widmeyertemplate.resource.domain.repository.ResourceRepository
import com.widmeyertemplate.resource.domain.repository.ResourceStringsRepository
import com.widmeyertemplate.utils.GradleUtils
import com.widmeyertemplate.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ResourceRepositoryImpl(private val project: Project) : ResourceRepository {
    private val basePath: String = project.basePath.orEmpty()
    private val stringsRepository: ResourceStringsRepository = ResourceStringsRepositoryImpl(basePath)
    private val colorsRepository: ResourceColorsRepository = ResourceColorsRepositoryImpl(basePath)
    private val fileRepository: FileRepository = FileRepositoryImpl()

    override fun update(): Flow<Result<Boolean, String>> = flow {
        try {
            stringsRepository.update()
            colorsRepository.update()

            deleteBuildDirs(basePath)
            GradleUtils.sync(project, basePath)
            emit(Result.Success(true))
        } catch (e: Throwable) {
            e.printStackTrace()
            emit(Result.Failure(e.message.orEmpty()))
        }
    }

    private fun deleteBuildDirs(basePath: String) {
        ResourceBuildsFiles.entries.forEach {
            val path = "$basePath/${it.beforePath}/${it.fileName}"
            fileRepository.deleteDir(path)
        }
    }
}