package com.widmeyertemplate.modules.data.repository

import com.intellij.openapi.externalSystem.service.execution.ProgressExecutionMode
import com.intellij.openapi.externalSystem.util.ExternalSystemUtil
import com.intellij.openapi.project.Project
import com.widmeyertemplate.modules.domain.model.ModuleData
import com.widmeyertemplate.modules.domain.model.files.ProjectFiles
import com.widmeyertemplate.modules.domain.repository.FileManagerRepository
import com.widmeyertemplate.modules.domain.repository.GradleRepository
import org.jetbrains.plugins.gradle.util.GradleConstants

class GradleRepositoryImpl(
    private val fileManagerRepository: FileManagerRepository
) : GradleRepository {
    override fun sync(project: Project, projectPath: String) {
        ExternalSystemUtil.refreshProject(
            project,
            GradleConstants.SYSTEM_ID,
            projectPath,
            false,
            ProgressExecutionMode.MODAL_SYNC,
        )
    }

    override fun addModuleToSettingsGradle(project: Project, moduleData: ModuleData) {
        val file = ProjectFiles.SETTINGS
        if (moduleData.isAddFeature) {
            val line = "include(\":androidApp:features:screen:$${moduleData.moduleName}\")"
            val featureRegex = """^\s*include\(":androidApp:features.*?"\)"""
            fileManagerRepository.findLineAndAdd(
                project = project,
                filePath = project.basePath.orEmpty(),
                fileName = file.getFullName(),
                patternRegex = featureRegex,
                lineToAdd = line,
                isNeedComma = false
            )
        }

        if (moduleData.isAddShared) {
            val line = "include(\":androidApp:features:screen:$${moduleData.moduleName}\")"
            val featureRegex = """^\s*include\(":shared*?"\)"""
            fileManagerRepository.findLineAndAdd(
                project = project,
                filePath = project.basePath.orEmpty(),
                fileName = file.getFullName(),
                patternRegex = featureRegex,
                lineToAdd = line,
                isNeedComma = false
            )
        }
    }

    override fun updateBuildGradleAndroid(project: Project, moduleData: ModuleData) {
        /*val fileType = Files.MODULE_BUILD_GRADLE
        val buildGradleFile = Path.of(androidAppPath, fileType.getFullName())

        if (!java.nio.file.Files.exists(buildGradleFile))
            throw IllegalStateException("$androidAppPath file not found in the project root")


        val virtualFile = buildGradleFile.toFile().toVirtualFile()
            ?: throw IllegalStateException("Не найдено ни одной зависимости projects.feature.* в build.gradle.kts")

        FileDocumentManager.getInstance().getDocument(virtualFile)?.let { document ->
            val text = document.text
            val featureRegex = Regex("""^\s*implementation\(projects\.feature\..*?\)""", RegexOption.MULTILINE)
            val fallbackRegex = Regex("""^\s*implementation\(.*?\)""", RegexOption.MULTILINE)
            val dependencyRegex = Regex("""dependencies \{""", RegexOption.MULTILINE)

            val match = featureRegex.findAll(text).lastOrNull()
                ?: fallbackRegex.findAll(text).lastOrNull()
                ?: dependencyRegex.findAll(text).lastOrNull()
                ?: throw IllegalStateException("Не найдено ни одной зависимости в Android build.gradle.kts")

            val insertOffset = match.range.last + 1
            val newLine = "\n\timplementation(projects.feature.$moduleName)"

            WriteCommandAction.runWriteCommandAction(project) {
                document.insertString(insertOffset, newLine)
            }
        }
         */
    }
}