package com.widmeyertemplate.modules.domain.repository

import com.intellij.openapi.project.Project
import com.widmeyertemplate.modules.domain.model.ModuleData

interface GradleRepository {
    fun sync(project: Project, projectPath: String)
    fun addModuleToSettingsGradle(project: Project, moduleData: ModuleData)
    fun updateBuildGradle(
        project: Project,
        path: String,
        fileName: String,
        regexes: List<String>,
        newLine: String,
        moduleData: ModuleData
    )
}