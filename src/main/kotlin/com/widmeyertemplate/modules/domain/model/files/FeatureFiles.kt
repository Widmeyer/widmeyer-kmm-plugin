package com.widmeyertemplate.modules.domain.model.files;

import com.widmeyertemplate.modules.domain.model.FileActionType

enum class FeatureFiles(): File {
    BUILD_GRADLE {
        override val fileName = "build.gradle"
        override val templatePath = "$startPathTemplate/build.gradle.kts.wt"
        override val extension = "kts"
        override val fileAction = FileActionType.MODULE
        override val beforePath = null
        override val afterName = null
        override val isLower: Boolean = false
    },
    GITIGNORE {
        override val fileName = ".gitignore"
        override val templatePath = "$startPathTemplate/gitignore.wt"
        override val extension = ""
        override val fileAction = FileActionType.COPY
        override val beforePath = null
        override val afterName = null
        override val isLower: Boolean = false
    },
    SCREEN {
        override val fileName = "FeatureScreen"
        override val templatePath = "$startPathTemplate/Screen.kt.wt"
        override val extension = "kt"
        override val fileAction = FileActionType.PACKAGE
        override val beforePath = null
        override val afterName = "Screen"
        override val isLower: Boolean = false
    },
    CONTENT {
        override val fileName = "FeatureScreenContent"
        override val templatePath = "$startPathTemplate/ScreenContent.kt.wt"
        override val extension = "kt"
        override val fileAction = FileActionType.PACKAGE
        override val beforePath = "components"
        override val afterName = "ScreenContent"
        override val isLower: Boolean = false
    };

    protected val startPathTemplate = "templates/jetpackScreen"
}