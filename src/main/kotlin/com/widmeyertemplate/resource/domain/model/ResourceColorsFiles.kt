package com.widmeyertemplate.resource.domain.model

import com.widmeyertemplate.utils.file.File
import com.widmeyertemplate.utils.file.FileActionType

enum class ResourceColorsFiles: File {
    COLORS_COMMON {
        override val fileName = "colors"
        override val templatePath = ""
        override val extension = "xml"
        override val fileAction = FileActionType.PACKAGE
        override val beforePath = "shared/resources/src/commonMain/moko-resources/colors"
        override val afterName = null
        override val isLower: Boolean = false
    },
    COLORS_ANDROID {
        override val fileName = "Colors"
        override val templatePath = ""
        override val extension = "kt"
        override val fileAction = FileActionType.PACKAGE
        override val beforePath = "$uiPath/main/kotlin"
        override val afterName = null
        override val isLower: Boolean = false
    },
    COLORS_ANDROID_XML {
        override val fileName = "colors"
        override val templatePath = ""
        override val extension = "xml"
        override val fileAction = FileActionType.PACKAGE
        override val beforePath = "$uiPath/res/values"
        override val afterName = null
        override val isLower: Boolean = false
    },
    COLORS_IOS {
        override val fileName = "Colors"
        override val templatePath = ""
        override val extension = "swift"
        override val fileAction = FileActionType.PACKAGE
        override val beforePath = "iosApp/iosApp/Resources"
        override val afterName = null
        override val isLower: Boolean = false
    };

    protected val uiPath = "androidApp/ui/src"
}