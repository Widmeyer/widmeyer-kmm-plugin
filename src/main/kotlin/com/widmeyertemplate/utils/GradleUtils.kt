package com.widmeyertemplate.utils

import com.intellij.openapi.externalSystem.service.execution.ProgressExecutionMode
import com.intellij.openapi.externalSystem.util.ExternalSystemUtil
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.gradle.util.GradleConstants

object GradleUtils {
    fun sync(project: Project, projectPath: String) {
        ExternalSystemUtil.refreshProject(
            project,
            GradleConstants.SYSTEM_ID,
            projectPath,
            false,
            ProgressExecutionMode.MODAL_SYNC,
        )
    }
}