package com.widmeyertemplate.modules.data.repository

import com.intellij.openapi.project.Project
import com.widmeyertemplate.utils.file.FileActionType
import com.widmeyertemplate.modules.domain.model.ModuleData
import com.widmeyertemplate.utils.Result
import com.widmeyertemplate.utils.TypeModel
import com.widmeyertemplate.modules.domain.model.files.FeatureFiles
import com.widmeyertemplate.utils.file.File
import com.widmeyertemplate.modules.domain.model.files.ProjectFiles
import com.widmeyertemplate.modules.domain.model.files.SharedFiles
import com.widmeyertemplate.modules.domain.repository.FileManagerRepository
import com.widmeyertemplate.modules.domain.repository.GradleRepository
import com.widmeyertemplate.modules.domain.repository.ModuleRepository
import com.widmeyertemplate.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.nio.file.Path

class ModuleRepositoryImpl : ModuleRepository {
    private val fileManagerRepository: FileManagerRepository = FileManagerRepositoryImpl()
    private val gradleRepository: GradleRepository = GradleRepositoryImpl(fileManagerRepository)

    override fun create(project: Project, moduleData: ModuleData): Flow<Result<Boolean, String>> = flow {
        try {
            moduleData.featurePath?.let { createModuleFeature(project = project, moduleData = moduleData) }
            moduleData.sharedPath?.let { createModuleShared(project = project, moduleData = moduleData) }
            insertLibrarySettings(project = project, moduleData = moduleData)

            gradleRepository.sync(project, projectPath = moduleData.rootPath)
            emit(Result.Success(true))
        } catch (e: Throwable) {
            e.printStackTrace()
            emit(Result.Failure(Constants.Modules.MODULE_ERROR_TITLE))
        }
    }

    private fun createModuleFeature(project: Project, moduleData: ModuleData) {
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

        addModuleToAndroidGradle(project = project, moduleData = moduleData)
        addImportsApp(project = project, moduleData = moduleData)
    }

    private fun createModuleShared(project: Project, moduleData: ModuleData) {
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

        addModuleToSharedGradle(project = project, moduleData = moduleData)
        addImportsShared(project = project, moduleData = moduleData)
    }

    private fun insertLibrarySettings(project: Project, moduleData: ModuleData) {
        val file = ProjectFiles.SETTINGS

        moduleData.featurePath?.let {
            gradleRepository.updateBuildGradle(
                project, path = moduleData.rootPath, fileName = file.getFullName(), regexes = listOf(
                    """^\s*include\(\":androidApp:.*?\)"""
                ),
                newLine = "\ninclude(\":androidApp:features:screen:${moduleData.moduleName}\")",
                moduleData = moduleData
            )
        }

        moduleData.sharedPath?.let {
            gradleRepository.updateBuildGradle(
                project, path = moduleData.rootPath, fileName = file.getFullName(), regexes = listOf(
                    """^\s*include\(\":shared:.*?\)"""
                ),
                newLine = "\ninclude(\":shared:features:${moduleData.moduleName}\")",
                moduleData = moduleData
            )
        }
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

    private fun addModuleToAndroidGradle(project: Project, moduleData: ModuleData) {
        val file = FeatureFiles.BUILD_GRADLE_APP
        val buildGradlePath = "${moduleData.rootPath}/${file.beforePath}"

        gradleRepository.updateBuildGradle(
            project, path = buildGradlePath, fileName = file.getFullName(), regexes = listOf(
                """(projects\.androidApp\.features\.screen\.[\w\.]+,)(?![\s\S]*projects\.androidApp\.features\.screen\.)"""
            ),
            newLine = "\n\tprojects.androidApp.features.screen.${moduleData.moduleName},",
            moduleData = moduleData
        )

        moduleData.sharedPath?.let {
            gradleRepository.updateBuildGradle(
                project, path = buildGradlePath, fileName = file.getFullName(), regexes = listOf(
                    """(projects\.shared\.features\.[\w\.]+,)(?![\s\S]*projects\.shared\.features\.)"""
                ),
                newLine = "\n\tprojects.shared.features.${moduleData.moduleName},",
                moduleData = moduleData
            )
        }
    }

    private fun addModuleToSharedGradle(project: Project, moduleData: ModuleData) {
        val file = SharedFiles.BUILD_GRADLE_APP
        val buildGradlePath = "${moduleData.rootPath}/${file.beforePath}"

        gradleRepository.updateBuildGradle(
            project, path = buildGradlePath, fileName = file.getFullName(), regexes = listOf(
                """^\s*implementation\(projects\.shared\..*?\)"""
            ),
            newLine = "\n\t\t\timplementation(projects.shared.features.${moduleData.moduleName})",
            moduleData = moduleData
        )

        val iosFile = SharedFiles.IOS
        val buildGradlePathIOS = "${moduleData.rootPath}/${iosFile.beforePath}"

        gradleRepository.updateBuildGradle(
            project, path = buildGradlePathIOS, fileName = iosFile.getFullName(), regexes = listOf(
                """(projects\.shared\.features\.[\w\.]+,)(?![\s\S]*projects\.shared\.features\.)"""
            ),
            newLine = "\n\tprojects.shared.features.${moduleData.moduleName},",
            moduleData = moduleData
        )
    }

    private fun addImportsApp(project: Project, moduleData: ModuleData) {
        val fileScreenEnum = SharedFiles.SCREEN
        val fileScreenPath =
            "${moduleData.rootPath}/${fileScreenEnum.beforePath}/${moduleData.projectName}/${fileScreenEnum.afterName}/${fileScreenEnum.getFullName()}"

        val fileNavHost = FeatureFiles.NAV_HOST
        val fileNavHostPath =
            "${moduleData.rootPath}/${fileNavHost.beforePath}/${moduleData.projectName}/${fileNavHost.afterName}/${fileNavHost.getFullName()}"

        fileManagerRepository.insertLine(
            project = project,
            filePath = Path.of(fileScreenPath),
            regex = """enum class Screen\s*\{[\s\S]*?\}""",
            importString = "\t${moduleData.featureName.uppercase()}\n",
            isInsertComma = true
        )

        fileManagerRepository.insertLine(
            project = project,
            filePath = Path.of(fileNavHostPath),
            regex = """when \(screen\)\s*\{[\s\S]*?\}""",
            importString = "\tScreen.${moduleData.featureName.uppercase()} -> ${moduleData.featureName}Screen()\n\t\t\t\t",
            isInsertComma = false
        )

        fileManagerRepository.insertImportLine(
            project = project,
            filePath = Path.of(fileNavHostPath),
            importLine = "import com.${moduleData.projectName}.screen.${moduleData.moduleName}.${moduleData.featureName}Screen"
        )
    }

    private fun addImportsShared(project: Project, moduleData: ModuleData) {
        val file = SharedFiles.DI_ROOT
        val filePath =
            "${moduleData.rootPath}/${file.beforePath}/${moduleData.projectName}/${file.afterName}/${file.getFullName()}"

        fileManagerRepository.insertLine(
            project = project,
            filePath = Path.of(filePath),
            regex = """modules\s*\([\s\S]*?\)""",
            importString = "\t${moduleData.moduleName}Module\n\t\t",
            isInsertComma = true
        )

        fileManagerRepository.insertImportLine(
            project = project,
            filePath = Path.of(filePath),
            importLine = "import com.${moduleData.projectName}.features.${moduleData.moduleName}.di.${moduleData.moduleName}Module"
        )
    }

    private fun skip() {}
}