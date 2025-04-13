package com.widmeyertemplate.modules.domain.model.files;

import com.widmeyertemplate.utils.file.File
import com.widmeyertemplate.utils.file.FileActionType

enum class SharedFiles : File {
    BUILD_GRADLE {
        override val fileName = "build.gradle"
        override val templatePath = "$startPathTemplate/build.gradle.kts.wt"
        override val extension = "kts"
        override val fileAction = FileActionType.MODULE
        override val beforePath = null
        override val afterName = null
        override val isLower: Boolean = false
    },
    SCREEN {
        override val fileName = "Screen"
        override val templatePath = ""
        override val extension = "kt"
        override val fileAction = FileActionType.NONE
        override val beforePath = "shared/features/base/src/commonMain/kotlin/com"
        override val afterName = "base/features/domain/enum"
        override val isLower: Boolean = false
    },
    BUILD_GRADLE_APP {
        override val fileName = "build.gradle"
        override val extension = "kts"
        override val templatePath = ""
        override val fileAction = FileActionType.NONE
        override val beforePath = startPathRoot
        override val afterName = null
        override val isLower: Boolean = false
    },
    REPOSITORY {
        override val fileName = "ScreenRepository"
        override val templatePath = "$startPathTemplate/ScreenRepository.kt.wt"
        override val extension = "kt"
        override val fileAction = FileActionType.PACKAGE
        override val beforePath = "domain"
        override val afterName = "ScreenRepository"
        override val isLower: Boolean = false
    },
    REPOSITORY_IMPL {
        override val fileName = "ScreenRepositoryImpl"
        override val templatePath = "$startPathTemplate/ScreenRepositoryImpl.kt.wt"
        override val extension = "kt"
        override val fileAction = FileActionType.PACKAGE
        override val beforePath = "data"
        override val afterName = "ScreenRepositoryImpl"
        override val isLower: Boolean = false
    },
    VIEW_MODEL {
        override val fileName = "ViewModel"
        override val templatePath = "$startPathTemplate/ScreenViewModel.kt.wt"
        override val extension = "kt"
        override val fileAction = FileActionType.PACKAGE
        override val beforePath = "presentation"
        override val afterName = "ViewModel"
        override val isLower: Boolean = false
    },
    DI {
        override val fileName = "ScreenModule"
        override val templatePath = "$startPathTemplate/screenModule.kt.wt"
        override val extension = "kt"
        override val fileAction = FileActionType.PACKAGE
        override val beforePath = "di"
        override val afterName = "Module"
        override val isLower: Boolean = true
    },
    DI_ROOT {
        override val fileName = "initKoin"
        override val extension = "kt"
        override val templatePath = ""
        override val beforePath = "$startPathRoot/src/commonMain/kotlin/com"
        override val afterName = "root/di"
        override val fileAction = FileActionType.NONE
        override val isLower: Boolean = false
    },
    IOS {
        override val fileName = "build.gradle"
        override val extension = "kts"
        override val templatePath = ""
        override val beforePath = "iosExport"
        override val afterName = ""
        override val fileAction = FileActionType.NONE
        override val isLower: Boolean = false
    };

    protected val startPathTemplate = "templates/viewModel"
    protected val startPathRoot = "shared/features/root"
}