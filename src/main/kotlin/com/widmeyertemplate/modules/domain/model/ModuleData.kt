package com.widmeyertemplate.modules.domain.model

data class ModuleData(
    val rootPath: String,
    val featurePath: String?,
    val sharedPath: String?,
    val projectName: String,
    val moduleName: String,
    val featureName: String
)
