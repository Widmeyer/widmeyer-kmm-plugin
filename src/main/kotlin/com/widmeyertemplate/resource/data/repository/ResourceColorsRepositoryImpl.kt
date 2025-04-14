package com.widmeyertemplate.resource.data.repository

import com.widmeyertemplate.resource.domain.model.ResourceColorsFiles
import com.widmeyertemplate.resource.domain.repository.ResourceColorsRepository

class ResourceColorsRepositoryImpl(private val rootPath: String): ResourceColorsRepository {
    private val fileRepository = FileRepositoryImpl()
    private val pathBase =
        "$rootPath/${ResourceColorsFiles.COLORS_COMMON.beforePath}/${ResourceColorsFiles.COLORS_COMMON.getFullName()}"

    override fun update() {
        insertAndroid()
        insertIOS()
    }

    private fun insertAndroid() {
        val file = ResourceColorsFiles.COLORS_ANDROID_XML
        val dirPath = file.beforePath
        val fileName = file.getFullName()

        fileRepository.replaceXml(sourcePath = pathBase, outputPath = "$rootPath/$dirPath/$fileName")

        val fileColors = ResourceColorsFiles.COLORS_ANDROID
        fileRepository.findFileAndReplaceColors(
            sourcePath = pathBase,
            outputPathDir = "$rootPath/${fileColors.beforePath}",
            fileName = fileColors.getFullName(),
            isSwift = false
        )
    }

    private fun insertIOS() {
        val fileColors = ResourceColorsFiles.COLORS_IOS
        fileRepository.findFileAndReplaceColors(
            sourcePath = pathBase,
            outputPathDir = "$rootPath/${fileColors.beforePath}",
            fileName = fileColors.getFullName(),
            isSwift = true
        )
    }
}