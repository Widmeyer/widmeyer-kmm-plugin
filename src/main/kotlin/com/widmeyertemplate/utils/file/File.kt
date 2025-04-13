package com.widmeyertemplate.utils.file

interface File {
    val fileName: String
    val templatePath: String?
    val beforePath: String?
    val afterName: String?
    val extension: String
    val fileAction: FileActionType
    val isLower: Boolean

    fun getFullName() = fileName + extension.takeIf { it.isNotBlank() }?.let { ".$it" }.orEmpty()
}