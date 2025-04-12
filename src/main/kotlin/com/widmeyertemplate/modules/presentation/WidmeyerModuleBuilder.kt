package com.widmeyertemplate.modules.presentation

import com.intellij.ide.util.projectWizard.EmptyModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.roots.ModifiableRootModel
import com.widmeyertemplate.utils.Constants
import org.jetbrains.kotlin.tools.projectWizard.plugins.Plugins
import org.jetbrains.kotlin.tools.projectWizard.wizard.IdeWizard
import org.jetbrains.kotlin.tools.projectWizard.wizard.service.IdeaServices

class WidmeyerModuleBuilder : EmptyModuleBuilder() {
    private var finishButtonClicked: Boolean = false
    private var wizardContext: WizardContext? = null
    private val wizard = IdeWizard(Plugins.allPlugins, IdeaServices.PROJECT_INDEPENDENT, isUnitTestMode = false)

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {}

    override fun getModuleType() = WidmeyerModuleType()
    override fun getPresentableName() = Constants.Modules.MODULE_MENU_NAME
    override fun getGroupName() = presentableName
    override fun getDescription() = ""
    override fun isTemplateBased() = false

    override fun modifySettingsStep(settingsStep: SettingsStep): ModuleWizardStep? {
        clickFinishButton()
        return null
    }

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable): ModuleWizardStep {
        wizardContext = context
        return WidmeyerModuleWizardStep(wizard, context!!)
    }

    private fun clickFinishButton() {
        if (finishButtonClicked) return
        finishButtonClicked = true
        wizardContext?.getNextButton()?.doClick()
    }
}