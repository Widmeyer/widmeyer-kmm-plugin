package com.widmeyertemplate.modules.data.repository

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.widmeyertemplate.modules.domain.model.ModuleData
import com.widmeyertemplate.modules.domain.model.files.ProjectFiles
import com.widmeyertemplate.modules.domain.repository.FileManagerRepository
import com.widmeyertemplate.modules.domain.repository.GradleRepository
import com.widmeyertemplate.utils.Constants
import com.widmeyertemplate.utils.GradleUtils
import org.jetbrains.kotlin.idea.core.util.toVirtualFile
import java.nio.file.Files
import java.nio.file.Path

class GradleRepositoryImpl(
    private val fileManagerRepository: FileManagerRepository
) : GradleRepository {
    override fun sync(project: Project, projectPath: String) =
        GradleUtils.sync(project = project, projectPath = projectPath)

    override fun addModuleToSettingsGradle(project: Project, moduleData: ModuleData) {
        val file = ProjectFiles.SETTINGS
        moduleData.featurePath?.let {
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

        moduleData.sharedPath?.let {
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

    override fun updateBuildGradle(
        project: Project,
        path: String,
        fileName: String,
        regexes: List<String>,
        newLine: String,
        moduleData: ModuleData
    ) {
        val buildGradleFile = Path.of(path, fileName)

        if (!Files.exists(buildGradleFile))
            throw IllegalStateException(Constants.Modules.MODULE_ERROR_TITLE)


        val virtualFile = buildGradleFile.toFile().toVirtualFile()
            ?: throw IllegalStateException(Constants.Modules.MODULE_ERROR_TITLE)

        FileDocumentManager.getInstance().getDocument(virtualFile)?.let { document ->
            val text = document.text
            var match: MatchResult? = null
            regexes.forEach {
                val regex = Regex(it, RegexOption.MULTILINE)
                val founded = regex.findAll(text).lastOrNull()
                if (founded != null) {
                    match = founded
                    return@forEach
                }
            }

            if (match == null) throw IllegalStateException(Constants.Modules.MODULE_ERROR_TITLE)

            val insertOffset = match!!.range.last + 1

            WriteCommandAction.runWriteCommandAction(project) {
                document.insertString(insertOffset, newLine)
            }
        }
    }
}