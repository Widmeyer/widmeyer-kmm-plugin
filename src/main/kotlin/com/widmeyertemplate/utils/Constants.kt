package com.widmeyertemplate.utils

import java.util.*

internal object Constants {
    private val bundle: ResourceBundle = ResourceBundle.getBundle("messages", Locale.getDefault())

    object Common {
        val IN_DEVELOPMENT: String get() = bundle.getString("inDevelopment")

        val SUCCESS: String get() = bundle.getString("success")
        val ERROR: String get() = bundle.getString("error.title")
    }

    object Modules {
        val DEFAULT_NAME = "widmeyer"
        val MODULE: String get() = bundle.getString("module")

        val CREATE_FEATURE_MODULE: String get() = bundle.getString("module.createFeature")
        val CREATE_SHARED_FEATURE_MODULE: String get() = bundle.getString("module.createSharedFeature")
        val MODULE_MENU_NAME: String get() = bundle.getString("module.menu.name")
        val MODULE_CREATE: String get() = bundle.getString("module.create")

        val MODULE_SELECT: String get() = bundle.getString("module.tooltip.createModule")
        val MODULE_SELECT_TITLE: String get() = bundle.getString("module.chooseDirectoryTitle")
        val MODULE_SELECT_PATH: String get() = bundle.getString("module.chooseDirectory")
        val MODULE_SELECT_PATH_LABEL: String get() = bundle.getString("module.chooseDirectoryLabel")

        val MODULE_SELECT_PROJECT: String get() = bundle.getString("module.select.project")
        val MODULE_SELECT_MODULE: String get() = bundle.getString("module.select.module")
        val MODULE_SELECT_FEATURE: String get() = bundle.getString("module.select.feature")

        val ERROR_NO_SELECTION: String get() = bundle.getString("module.error.noSelection")
        val MODULE_ERROR_TITLE: String get() = bundle.getString("module.error.moduleCreation")

        val VALIDATION_NO_MODULE_SELECTED: String get() = bundle.getString("module.validation.noModuleSelected")
        val VALIDATION_EMPTY_PROJECT_NAME: String get() = bundle.getString("module.validation.emptyProjectName")
        val VALIDATION_EMPTY_MODULE_NAME: String get() = bundle.getString("module.validation.emptyModuleName")
        val VALIDATION_EMPTY_FEATURE_NAME: String get() = bundle.getString("module.validation.emptyFeatureName")
    }
    object Resources {
        val ACTION_BUTTON: String get() = bundle.getString("resources.button")
        val NOT_FOUND: String get() = bundle.getString("resources.notFound")
    }
}