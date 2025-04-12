package com.widmeyertemplate.modules.domain.model.files;

import com.widmeyertemplate.modules.domain.model.FileActionType

enum class ProjectFiles(
    val fileName: String,
    val templatePath: String?,
    val beforePath: String? = null,
    val afterName: String? = null,
    val extension: String,
    val fileAction: FileActionType = FileActionType.NONE
) {
    SETTINGS(
        fileName = "settings.gradle",
        extension = "kts",
        templatePath = null
    );

    fun getFullName(): String = "$fileName.$extension"
}