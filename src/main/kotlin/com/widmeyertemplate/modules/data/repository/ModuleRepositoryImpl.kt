package com.widmeyertemplate.modules.data.repository

import com.intellij.openapi.project.Project
import com.widmeyertemplate.modules.domain.model.FileActionType
import com.widmeyertemplate.modules.domain.model.ModuleData
import com.widmeyertemplate.modules.domain.model.Result
import com.widmeyertemplate.modules.domain.model.TypeModel
import com.widmeyertemplate.modules.domain.model.files.FeatureFiles
import com.widmeyertemplate.modules.domain.model.files.File
import com.widmeyertemplate.modules.domain.model.files.SharedFiles
import com.widmeyertemplate.modules.domain.repository.FileManagerRepository
import com.widmeyertemplate.modules.domain.repository.GradleRepository
import com.widmeyertemplate.modules.domain.repository.ModuleRepository
import com.widmeyertemplate.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ModuleRepositoryImpl : ModuleRepository {
    private val fileManagerRepository: FileManagerRepository = FileManagerRepositoryImpl()
    private val gradleRepository: GradleRepository = GradleRepositoryImpl(fileManagerRepository)

    override fun create(project: Project, moduleData: ModuleData): Flow<Result<String, String>> = flow {
        try {
            moduleData.featurePath?.let { createModuleFeature(moduleData = moduleData) }
            moduleData.sharedPath?.let { createModuleShared(moduleData = moduleData) }

            // gradleRepository.sync(project, projectPath = moduleData.rootPath)
        } catch (e: Throwable) {
            e.printStackTrace()
            emit(Result.Failure(Constants.Modules.MODULE_ERROR_TITLE))
        }
    }

    private fun createModuleFeature(moduleData: ModuleData) {
        val modulePath = "${moduleData.rootPath}/androidApp/features/screen/${moduleData.moduleName}"
        fileManagerRepository.createDir(modulePath)
        copy(
            files = FeatureFiles.entries.toTypedArray(),
            typeModel = TypeModel.FEATURE,
            modulePath = modulePath,
            projectName = moduleData.projectName,
            moduleName = moduleData.moduleName,
            featureName = moduleData.featureName
        )
    }

    private fun createModuleShared(moduleData: ModuleData) {
        val modulePath = "${moduleData.rootPath}/shared/features/${moduleData.moduleName}"
        fileManagerRepository.createDir(modulePath)
        copy(
            files = SharedFiles.entries.toTypedArray(),
            typeModel = TypeModel.SHARED,
            modulePath = modulePath,
            projectName = moduleData.projectName,
            moduleName = moduleData.moduleName,
            featureName = moduleData.featureName
        )
    }

    private fun copy(
        files: Array<out File>,
        typeModel: TypeModel,
        modulePath: String,
        projectName: String,
        moduleName: String,
        featureName: String,
    ) {
        val srcMain = typeModel.srcMain
        val packageAdditionalName = typeModel.packageAdditionalName
        val packageName = "$modulePath/src/${srcMain}/kotlin/com/$projectName/${packageAdditionalName}/$moduleName"

        files.forEach { file ->
            when (file.fileAction) {
                FileActionType.NONE -> skip()
                FileActionType.COPY -> fileManagerRepository.copyFile(
                    outputPath = "$modulePath/${file.getFullName()}",
                    templatePath = file.templatePath.orEmpty()
                )

                FileActionType.MODULE -> {
                    val outputPath = "$modulePath/${file.getFullName()}"
                    fileManagerRepository.insertFile(
                        outputPath = outputPath,
                        templatePath = file.templatePath.orEmpty(),
                        moduleName = moduleName,
                        projectName = projectName,
                        featureName = featureName
                    )
                }

                FileActionType.PACKAGE -> {
                    val featureNameFormatted = if (file.isLower) featureName.lowercase() else featureName
                    val beforePath =
                        packageName + file.beforePath.takeIf { !it.isNullOrBlank() }?.let { "/$it" }.orEmpty()
                    val afterPath = "$featureNameFormatted${file.afterName}.${file.extension}"
                    val outputPath = "$beforePath/$afterPath"

                    fileManagerRepository.insertFile(
                        outputPath = outputPath,
                        templatePath = file.templatePath.orEmpty(),
                        projectName = projectName,
                        moduleName = moduleName,
                        featureName = featureName
                    )
                }
            }
        }
    }

    private fun skip() {}
}