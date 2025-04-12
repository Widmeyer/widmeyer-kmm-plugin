package com.widmeyertemplate.modules.domain.repository

import com.intellij.openapi.project.Project

interface FileManagerRepository {
    fun findLineAndAdd(
        project: Project,
        filePath: String,
        fileName: String,
        patternRegex: String,
        lineToAdd: String,
        isNeedComma: Boolean
    )

    fun addToFile(filePath: String, fileName: String, lineToAdd: String)
    fun copyFile(outputPath: String, templatePath: String)
    fun insertFile(
        outputPath: String,
        templatePath: String,
        projectName: String,
        moduleName: String,
        featureName: String
    )

    fun createDir(path: String)
}