package com.widmeyertemplate.modules.presentation

import com.android.sdklib.SdkVersionInfo.LOWEST_ACTIVE_API
import com.android.tools.idea.npw.model.ProjectSyncInvoker
import com.android.tools.idea.npw.module.ModuleGalleryEntry
import com.android.tools.idea.wizard.model.SkippableWizardStep
import com.android.tools.idea.wizard.template.Category
import com.android.tools.idea.wizard.template.FormFactor
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.widmeyertemplate.utils.Constants


class WidmeyerModuleGalleryEntry(private val onSuccess: (Project) -> Unit) : ModuleGalleryEntry {
    override val name = Constants.Modules.MODULE_MENU_NAME
    override val description = Constants.Modules.MODULE_CREATE
    override val icon = AllIcons.Icons.Ide.NextStep

    private var step: WidmeyerModuleAndroidStep? = null

    override fun createStep(
        project: Project,
        moduleParent: String,
        projectSyncInvoker: ProjectSyncInvoker
    ): SkippableWizardStep<*> {
        val model = AndroidModuleModel.fromExistingProject(
            project,
            moduleParent,
            projectSyncInvoker,
            FormFactor.Mobile,
            Category.Other,
        )

        step = WidmeyerModuleAndroidStep(
            model = model,
            basePackage = Constants.Modules.DEFAULT_NAME,
            minSdkLevel = LOWEST_ACTIVE_API,
            title = Constants.Modules.MODULE_SELECT_MODULE,
            onSuccess = onSuccess
        )

        return step!!
    }

    fun getData() = step?.getData()
}