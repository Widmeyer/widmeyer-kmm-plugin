package com.widmeyertemplate.modules.data.repository

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.widmeyertemplate.modules.domain.repository.FileManagerRepository
import org.jetbrains.kotlin.idea.core.util.toVirtualFile
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption

class FileManagerRepositoryImpl : FileManagerRepository {
    override fun findLineAndAdd(
        project: Project,
        filePath: String,
        fileName: String,
        patternRegex: String,
        lineToAdd: String,
        isNeedComma: Boolean
    ) {
        val path = Path.of(filePath, fileName)

        val virtualFile = path.toFile().toVirtualFile() ?: return
        val document = FileDocumentManager.getInstance().getDocument(virtualFile) ?: return
        val text = document.text

        val fileRegex = Regex(patternRegex)
        val modulesMatch = fileRegex.find(text)

        if (modulesMatch != null) {
            val modulesBlock = modulesMatch.groupValues.getOrNull(0) ?: return

            if (!modulesBlock.contains(lineToAdd)) {
                val closingBracketIndex = modulesMatch.range.last
                val beforeClosing = text.substring(0, closingBracketIndex)
                val lastNonEmptyLine = beforeClosing.lines().lastOrNull { it.trim().isNotEmpty() }
                if (isNeedComma && lastNonEmptyLine != null && !lastNonEmptyLine.trim().endsWith(",")) {
                    WriteCommandAction.runWriteCommandAction(project) {
                        val lastIndex = text.indexOf(lastNonEmptyLine)
                        document.insertString(lastIndex + lastNonEmptyLine.length, ",")
                    }
                }

                WriteCommandAction.runWriteCommandAction(project) {
                    val indexToInsertNewModule = modulesMatch.range.last - 1
                    document.insertString(indexToInsertNewModule, lineToAdd)
                }
            }
        }
    }

    override fun addToFile(filePath: String, fileName: String, lineToAdd: String) {
        val settingsFile = Path.of(filePath, fileName)

        if (Files.exists(settingsFile)) Files.write(
            settingsFile,
            "\n$lineToAdd".toByteArray(),
            StandardOpenOption.APPEND
        ) else throw IllegalStateException("$fileName file not found in the project root")
    }

    override fun copyFile(outputPath: String, templatePath: String) {
        val path = Path.of(outputPath)
        val inputStream: InputStream = javaClass.classLoader.getResourceAsStream(templatePath)
            ?: throw IllegalStateException("Resource not found: $templatePath")

        Files.createDirectories(path.parent)

        Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING)
    }

    override fun insertFile(
        outputPath: String,
        templatePath: String,
        projectName: String,
        moduleName: String,
        featureName: String
    ) {
        val path = Path.of(outputPath)
        val inputStream: InputStream = javaClass.classLoader.getResourceAsStream(templatePath)
            ?: throw IllegalStateException("Resource not found: $templatePath")

        val text = inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
        inputStream.close()
        val updatedText = text
            .replace("\${PROJECT_NAME}", projectName)
            .replace("\${MODULE_NAME}", moduleName)
            .replace("\${FEATURE_NAME}", featureName)

        val byteArray = updatedText.byteInputStream(Charsets.UTF_8)
        byteArray.close()

        Files.createDirectories(path.parent)

        Files.copy(byteArray, path, StandardCopyOption.REPLACE_EXISTING)
    }

    override fun createDir(path: String) {
        val moduleDir = File(path)

        if (!moduleDir.exists()) moduleDir.mkdirs()
    }
}