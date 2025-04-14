package com.widmeyertemplate.resource.data.repository

import com.widmeyertemplate.resource.domain.repository.FileRepository
import com.widmeyertemplate.utils.Constants
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.io.path.pathString

class FileRepositoryImpl : FileRepository {
    override fun deleteDir(buildSrc: String) {
        val file = File(buildSrc)
        FileUtils.deleteDirectory(file)
    }

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

    override fun findFileAndReplaceColors(
        sourcePath: String,
        outputPathDir: String,
        fileName: String,
        isSwift: Boolean
    ) {
        val xmlPath = Path.of(sourcePath)
        val colorsInXml = parseColorsFromXml(xmlPath)

        Files.walk(Path.of(outputPathDir))
            .filter { path ->
                !path.toString().contains("/build/") && path.fileName.toString() == fileName
            }
            .forEach { path ->
                val fileContent = Files.readString(path)

                var updatedContent = fileContent

                colorsInXml.forEach { color ->
                    updatedContent = updatedContent.replace(Regex(color), "")
                }

                val generatedBlock = if (isSwift) generateColorBlocksForSwift(colorsInXml)
                else generateColorBlocksForKotlin(colorsInXml)

                updatedContent = insertGeneratedBlock(updatedContent, generatedBlock)

                Files.writeString(path, updatedContent)
            }
    }

    private fun parseColorsFromXml(path: Path): List<String> {
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Files.newInputStream(path))
        val nodeList = document.getElementsByTagName("color")
        val colors = mutableListOf<String>()

        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            colors.add(node.attributes.getNamedItem("name").nodeValue)
        }

        return colors
    }


    private fun generateColorBlocksForKotlin(colors: List<String>): String {
        val abstractBlock = colors.joinToString("\n") { "\tabstract val $it: Color" }
        val lightBlock =
            colors.joinToString("\n") { "\t\toverride val $it: Color = Color(MultiplatformResource.colors.$it.getColor(context))," }
        val darkBlock =
            colors.joinToString("\n") { "\t\toverride val $it: Color = Color(MultiplatformResource.colors.$it.getColor(context))," }

        return """
sealed class Colors {
$abstractBlock

    data class Light(
        private val context: Context,
$lightBlock
    ): Colors()

    data class Dark(
        private val context: Context, 
$darkBlock
    ): Colors()
}
""".trimIndent()
    }

    private fun generateColorBlocksForSwift(colors: List<String>): String {
        val abstractBlock = colors.joinToString("\n") { "\tvar $it: SwiftUI.Color { fatalError(\"Must override\") }" }
        val lightBlock: String =
            colors.joinToString("\n") { "\toverride var $it: SwiftUI.Color { MultiplatformResource.colors.$it.getColor() }" }
        val darkBlock =
            colors.joinToString("\n") { "\toverride var $it: SwiftUI.Color { MultiplatformResource.colors.$it.getColor() }" }

        return """
class Colors {
$abstractBlock
}

class LightColors: Colors {
$lightBlock
}

class DarkColors: Colors {
$darkBlock
}
""".trimIndent()
    }

    private fun insertGeneratedBlock(content: String, generated: String): String {
        val packageLine = content.lineSequence().firstOrNull { it.startsWith("package") }
        val importLines = content.lineSequence().filter { it.startsWith("import") }

        val header = buildString {
            if (packageLine != null) appendLine(packageLine)
            importLines.forEach { appendLine(it) }
        }

        return header + "\n" + generated
    }

}