package com.widmeyertemplate.resource.domain.repository

interface FileRepository {
    fun replaceXml(sourcePath: String, outputPath: String)
    fun findFileAndReplace(sourcePath: String, outputPathDir: String, nameFile: String)
}