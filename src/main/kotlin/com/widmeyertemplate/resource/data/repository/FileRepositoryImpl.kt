package com.widmeyertemplate.resource.data.repository

import com.widmeyertemplate.resource.domain.repository.FileRepository
import com.widmeyertemplate.utils.Constants
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.pathString

class FileRepositoryImpl : FileRepository {
    override fun replaceXml(sourcePath: String, outputPath: String) {
        val source = Path.of(sourcePath)
        val target = Path.of(outputPath)

        if (!Files.exists(source)) throw IllegalStateException("${Constants.Resources.NOT_FOUND}: $sourcePath")


        Files.createDirectories(target.parent)

        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING)
    }

    override fun findFileAndReplace(sourcePath: String, outputPathDir: String, nameFile: String) {
        val sourceFile = Path.of(sourcePath)

        if (!Files.exists(sourceFile)) throw IllegalStateException("${Constants.Resources.NOT_FOUND}: $sourcePath")

        Files.walk(Path.of(outputPathDir))
            .filter { path ->
                !path.toString().contains("/build/") &&
                        path.fileName.toString() == nameFile
            }
            .forEach { path ->
                if (path.pathString != sourcePath) {
                    Files.deleteIfExists(path)
                    Files.createDirectories(path.parent)
                    Files.copy(sourceFile, path, StandardCopyOption.REPLACE_EXISTING)
                }
            }
    }
}