package com.widmeyertemplate.resource.domain.model

import com.widmeyertemplate.utils.file.File
import com.widmeyertemplate.utils.file.FileActionType

enum class ResourceBuildsFiles: File {
    BUILD_UI {
        override val fileName = "build"
        override val extension = ""
        override val fileAction = FileActionType.NONE
        override val templatePath = null
        override val beforePath = "androidApp/ui"
        override val afterName = null
        override val isLower: Boolean = false
    },
    BUILD_SHARED {
        override val fileName = "build"
        override val extension = ""
        override val fileAction = FileActionType.NONE
        override val templatePath = null
        override val beforePath = "shared"
        override val afterName = null
        override val isLower: Boolean = false
    },
    BUILD_RES {
        override val fileName = "build"
        override val extension = ""
        override val fileAction = FileActionType.NONE
        override val templatePath = null
        override val beforePath = "shared/resources"
        override val afterName = null
        override val isLower: Boolean = false
    };
}