package com.widmeyertemplate.modules.domain.model.files;

import com.widmeyertemplate.modules.domain.model.FileActionType

enum class SharedFiles(
    val fileName: String,
    val templatePath: String?,
    val beforePath: String? = null,
    val afterName: String? = null,
    val extension: String,
    val fileAction: FileActionType = FileActionType.NONE
) {
    CONSUMERS(
        fileName = "consumer-rules",
        templatePath = "template/consumer-rules.pro",
        fileAction = FileActionType.COPY,
        extension = ".pro"
    );

    fun getFullName(): String = "$fileName.$extension"
}