package com.widmeyertemplate.resource.domain.repository

interface FileRepository {
    fun deleteDir(buildSrc: String)
    fun replaceXml(sourcePath: String, outputPath: String)
    fun findFileAndReplace(sourcePath: String, outputPathDir: String, nameFile: String)
    fun findFileAndReplaceColors(
        sourcePath: String,
        outputPathDir: String,
        fileName: String,
        isSwift: Boolean
    )
}