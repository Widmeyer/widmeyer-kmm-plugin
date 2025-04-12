package com.widmeyertemplate.modules.presentation

import com.intellij.ide.util.projectWizard.EmptyModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.ui.Messages
import com.widmeyertemplate.modules.data.repository.ModuleRepositoryImpl
import com.widmeyertemplate.modules.domain.repository.ModuleRepository
import com.widmeyertemplate.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.kotlin.tools.projectWizard.plugins.Plugins
import org.jetbrains.kotlin.tools.projectWizard.wizard.IdeWizard
import org.jetbrains.kotlin.tools.projectWizard.wizard.service.IdeaServices
import com.widmeyertemplate.modules.domain.model.Result

class WidmeyerModuleBuilder : EmptyModuleBuilder() {
    private var moduleRepository: ModuleRepository = ModuleRepositoryImpl()
    private var finishButtonClicked: Boolean = false
    private var wizardContext: WizardContext? = null
    private val wizard = IdeWizard(Plugins.allPlugins, IdeaServices.PROJECT_INDEPENDENT, isUnitTestMode = false)
    private var step: WidmeyerModuleWizardStep? = null

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {}

    override fun getModuleType() = WidmeyerModuleType()
    override fun getPresentableName() = Constants.Modules.MODULE_MENU_NAME
    override fun getGroupName() = presentableName
    override fun getDescription() = ""
    override fun isTemplateBased() = false

    override fun modifySettingsStep(settingsStep: SettingsStep): ModuleWizardStep? {
        clickFinishButton()

        wizardContext?.project?.let { project ->
            step?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    moduleRepository.create(project = project, moduleData = it.getData()).collect {
                        when (it) {
                            is Result.Success -> Messages.showMessageDialog(
                                it.data,
                                Constants.Common.SUCCESS,
                                Messages.getInformationIcon()
                            )

                            is Result.Failure -> Messages.showMessageDialog(
                                it.error,
                                Constants.Common.ERROR,
                                Messages.getErrorIcon()
                            )
                        }
                    }
                }

            }
        }

        return null
    }

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable): ModuleWizardStep? {
        wizardContext = context
        step = WidmeyerModuleWizardStep(wizard, context!!)
        return step
    }

    private fun clickFinishButton() {
        if (finishButtonClicked) return
        finishButtonClicked = true
        wizardContext?.getNextButton()?.doClick()
    }
}