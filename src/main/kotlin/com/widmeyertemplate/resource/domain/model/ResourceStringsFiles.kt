package com.widmeyertemplate.resource.domain.model

import com.widmeyertemplate.utils.file.File
import com.widmeyertemplate.utils.file.FileActionType

enum class ResourceStringsFiles: File {
    SHARED_XML {
        override val fileName = "strings"
        override val templatePath = ""
        override val extension = "xml"
        override val fileAction = FileActionType.PACKAGE
        override val beforePath = "$commonPath"
        override val afterName = null
        override val isLower: Boolean = false
    },
    ANDROID_XML {
        override val fileName = "strings"
        override val templatePath = ""
        override val extension = "xml"
        override val fileAction = FileActionType.PACKAGE
        override val beforePath = "$uiPath/res/values"
        override val afterName = null
        override val isLower: Boolean = false
    };

    protected val commonPath = "shared/resources/src/commonMain/moko-resources"
    protected val uiPath = "androidApp/ui/src/main"
}