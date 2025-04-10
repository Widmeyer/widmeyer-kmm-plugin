package com.widmeyertemplate.feature

import com.intellij.icons.AllIcons
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager
import com.intellij.openapi.roots.ui.configuration.ModulesProvider

class FeatureModuleType : ModuleType<FeatureModuleBuilder>(ID) {
    override fun createModuleBuilder() = FeatureModuleBuilder()

    override fun getName() = "Widmeyer Template"

    override fun getDescription() = "Create Android Model"

    override fun getNodeIcon(b: Boolean) = AllIcons.Icons.Ide.NextStep

    override fun createWizardSteps(
        wizardContext: WizardContext,
        moduleBuilder: FeatureModuleBuilder,
        modulesProvider: ModulesProvider
    ): Array<ModuleWizardStep> = super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider)

    companion object {
        private const val ID = "FEATURE_MODULE_TYPE"

        @JvmStatic
        fun getInstance() = ModuleTypeManager.getInstance().findByID(ID) as FeatureModuleType
    }

}
