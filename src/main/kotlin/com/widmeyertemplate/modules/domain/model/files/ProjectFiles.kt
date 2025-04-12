package com.widmeyertemplate.modules.domain.model.files;

import com.widmeyertemplate.modules.domain.model.FileActionType

enum class ProjectFiles : File {
    SETTINGS {
        override val fileName = "settings.gradle"
        override val extension = "kts"
        override val fileAction = FileActionType.COPY
        override val templatePath = null
        override val beforePath = null
        override val afterName = null
        override val isLower: Boolean = false
    };
}