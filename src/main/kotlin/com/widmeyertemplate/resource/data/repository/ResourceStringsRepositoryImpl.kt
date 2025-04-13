package com.widmeyertemplate.resource.data.repository

import com.widmeyertemplate.resource.domain.model.ResourceStringsFiles
import com.widmeyertemplate.resource.domain.repository.FileRepository
import com.widmeyertemplate.resource.domain.repository.ResourceStringsRepository

class ResourceStringsRepositoryImpl(val rootPath: String) : ResourceStringsRepository {
    private val fileRepository: FileRepository = FileRepositoryImpl()
    private val pathBase =
        "$rootPath/${ResourceStringsFiles.SHARED_XML.beforePath}/base/${ResourceStringsFiles.SHARED_XML.getFullName()}"

    override fun update() {
        insertAndroid()
        insertShared()
    }

    private fun insertAndroid() {
        val file = ResourceStringsFiles.ANDROID_XML
        val dirPath = file.beforePath
        val fileName = file.getFullName()

        fileRepository.replaceXml(sourcePath = pathBase, outputPath = "$rootPath/$dirPath/$fileName")
    }

    private fun insertShared() {
        val file = ResourceStringsFiles.SHARED_XML

        fileRepository.findFileAndReplace(
            sourcePath = pathBase,
            outputPathDir = "$rootPath/${ResourceStringsFiles.SHARED_XML.beforePath}",
            nameFile = file.getFullName()
        )
    }
}