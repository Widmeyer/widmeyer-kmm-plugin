package com.widmeyertemplate.modules.domain.repository

import com.intellij.openapi.project.Project
import com.widmeyertemplate.modules.domain.model.ModuleData
import com.widmeyertemplate.utils.Result
import kotlinx.coroutines.flow.Flow

interface ModuleRepository {
    fun create(project: Project, moduleData: ModuleData): Flow<Result<String, String>>
}