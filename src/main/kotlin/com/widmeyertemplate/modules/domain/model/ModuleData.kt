package com.widmeyertemplate.modules.domain.model

data class ModuleData(
    val isAddFeature: Boolean,
    val isAddShared: Boolean,
    val rootPath: String,
    val projectName: String,
    val moduleName: String,
    val featureName: String
)
