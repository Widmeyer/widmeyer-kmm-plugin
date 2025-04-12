package com.widmeyertemplate.modules.presentation

import com.intellij.icons.AllIcons
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import com.widmeyertemplate.utils.Constants

class WidmeyerModuleType : ModuleType<WidmeyerModuleBuilder>(ID) {
    override fun createModuleBuilder() = WidmeyerModuleBuilder()

    override fun getName() = Constants.Modules.MODULE_MENU_NAME

    override fun getDescription() = Constants.Modules.MODULE_CREATE

    override fun getNodeIcon(b: Boolean) = AllIcons.Icons.Ide.NextStep

    companion object { private const val ID = "FEATURE_MODULE_TYPE" }

    override fun createWizardSteps(
        wizardContext: WizardContext,
        moduleBuilder: WidmeyerModuleBuilder,
        modulesProvider: ModulesProvider
    ): Array<ModuleWizardStep> = emptyArray()
}
