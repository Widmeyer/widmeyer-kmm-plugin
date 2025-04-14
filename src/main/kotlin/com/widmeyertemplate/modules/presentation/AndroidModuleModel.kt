package com.widmeyertemplate.modules.presentation

import com.android.tools.idea.npw.model.ExistingProjectModelData
import com.android.tools.idea.npw.model.ProjectModelData
import com.android.tools.idea.npw.model.ProjectSyncInvoker
import com.android.tools.idea.npw.module.ModuleModel
import com.android.tools.idea.npw.project.GradleAndroidModuleTemplate.createSampleTemplate
import com.android.tools.idea.npw.template.ModuleTemplateDataBuilder
import com.android.tools.idea.observable.core.ObjectProperty
import com.android.tools.idea.observable.core.ObjectValueProperty
import com.android.tools.idea.projectsystem.NamedModuleTemplate
import com.android.tools.idea.templates.recipe.DefaultRecipeExecutor
import com.android.tools.idea.wizard.template.*
import com.google.wireless.android.sdk.stats.AndroidStudioEvent.TemplateRenderer
import com.google.wireless.android.sdk.stats.AndroidStudioEvent.TemplatesUsage.TemplateComponent.WizardUiContext
import com.google.wireless.android.sdk.stats.AndroidStudioEvent.TemplatesUsage.TemplateComponent.WizardUiContext.NEW_MODULE
import com.intellij.openapi.project.Project
import com.widmeyertemplate.utils.Constants

class AndroidModuleModel(
    override val formFactor: ObjectProperty<FormFactor>,
    override val category: ObjectProperty<Category>,
    override val isLibrary: Boolean = false,
    projectModelData: ProjectModelData,
    template: NamedModuleTemplate,
    moduleParent: String,
    wizardContext: WizardUiContext
) : ModuleModel(
    name = "",
    commandName = "",
    isLibrary = false,
    projectModelData = projectModelData,
    _template = template,
    moduleParent = moduleParent,
    wizardContext = wizardContext
) {
    override val moduleTemplateDataBuilder = ModuleTemplateDataBuilder(
        projectTemplateDataBuilder = projectTemplateDataBuilder,
        isNewModule = true,
        viewBindingSupport = viewBindingSupport.getValueOr(ViewBindingSupport.SUPPORTED_4_0_MORE)
    )

    override val renderer = ModuleTemplateRenderer()

    init {
        if (applicationName.isEmpty.get()) applicationName.set(Constants.Modules.DEFAULT_NAME)
    }

    inner class ModuleTemplateRenderer : ModuleModel.ModuleTemplateRenderer() {
        override val recipe: Recipe
            get() = { data: TemplateData ->
                if (this is DefaultRecipeExecutor) {
                    println("ABOBA")
                }
            }
    }

    companion object {
        fun fromExistingProject(
            project: Project,
            moduleParent: String,
            projectSyncInvoker: ProjectSyncInvoker,
            formFactor: FormFactor,
            category: Category,
        ): AndroidModuleModel = AndroidModuleModel(
            projectModelData = ExistingProjectModelData(project, projectSyncInvoker),
            template = createSampleTemplate(),
            moduleParent = moduleParent,
            wizardContext = NEW_MODULE,
            formFactor = ObjectValueProperty(formFactor),
            category = ObjectValueProperty(category),
        )
    }

    override val loggingEvent = TemplateRenderer.ANDROID_MODULE
}