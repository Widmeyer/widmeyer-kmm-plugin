package com.widmeyertemplate.modules.presentation

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.AbstractWizard
import com.intellij.openapi.options.ConfigurationException
import com.widmeyertemplate.modules.domain.model.ModuleData
import com.widmeyertemplate.utils.TypeModel
import com.widmeyertemplate.modules.presentation.components.ModuleSetupPanel
import com.widmeyertemplate.utils.Constants
import org.jdesktop.swingx.VerticalLayout
import org.jetbrains.kotlin.tools.projectWizard.core.entity.ValidationResult
import org.jetbrains.kotlin.tools.projectWizard.phases.GenerationPhase
import org.jetbrains.kotlin.tools.projectWizard.wizard.IdeWizard
import org.jetbrains.kotlin.tools.projectWizard.wizard.KotlinNewProjectWizardUIBundle
import org.jetbrains.kotlin.tools.projectWizard.wizard.WizardStep
import java.awt.Dimension
import java.awt.Font
import javax.swing.*


class WidmeyerModuleWizardStep(
    wizard: IdeWizard,
    private val wizardContext: WizardContext,
) : WizardStep(wizard, GenerationPhase.SECOND_STEP) {
    private val panel: JPanel
    private val featurePanel =
        ModuleSetupPanel(basePath = wizardContext.project?.basePath.orEmpty(), typeModel = TypeModel.FEATURE)
    private val sharedPanel =
        ModuleSetupPanel(basePath = wizardContext.project?.basePath.orEmpty(), typeModel = TypeModel.SHARED)
    private val projectNameField: JTextField
    private val moduleNameField: JTextField
    private val featureNameField: JTextField

    init {
        panel = JPanel().apply {
            layout = VerticalLayout()
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

            add(JLabel(Constants.Modules.MODULE).apply {
                font = Font("Arial", Font.BOLD, 14)
            })
        }

        projectNameField = getField(Constants.Modules.MODULE_SELECT_PROJECT)
        moduleNameField = getField(Constants.Modules.MODULE_SELECT_MODULE)
        featureNameField = getField(Constants.Modules.MODULE_SELECT_FEATURE)

        panel.add(featurePanel)
        panel.add(sharedPanel)
    }

    private fun getField(label: String): JTextField {
        panel.add(JLabel(label))
        val textField = JTextField().apply {
            preferredSize = Dimension(480, 32)
            maximumSize = preferredSize
        }

        panel.add(textField)
        return textField
    }

    override fun getComponent() = panel

    override fun updateDataModel() {
        try {
            validateOrThrow()
        } catch (ex: IllegalArgumentException) {
            JOptionPane.showMessageDialog(panel, ex.message, Constants.Common.ERROR, JOptionPane.ERROR_MESSAGE)
        }
    }

    override fun validate(): Boolean {
        return try {
            validateOrThrow()
            true
        } catch (ex: IllegalArgumentException) {
            handleErrors(error = ValidationResult.ValidationError(ex.message.orEmpty()))
        }
    }

    override fun handleErrors(error: ValidationResult.ValidationError) =
        throw ConfigurationException(error.messages.firstOrNull(), Constants.Modules.MODULE_ERROR_TITLE)

    private fun validateOrThrow() {
        val isEnabledFeature = featurePanel.isEnabled
        val isEnabledShared = sharedPanel.isEnabled

        validateInput()

        if ((!isEnabledFeature && !isEnabledShared)) throw IllegalArgumentException(Constants.Modules.VALIDATION_NO_MODULE_SELECTED)
    }

    private fun validateInput() {
        val project = projectNameField.text
        val module = moduleNameField.text
        val feature = featureNameField.text

        when {
            project.isEmpty() || project.contains(Regex("[^a-zA-Z0-9_]")) ->
                throw IllegalArgumentException(Constants.Modules.VALIDATION_EMPTY_PROJECT_NAME)


            module.isEmpty() || module.contains(Regex("[^a-z0-9_]")) ->
                throw IllegalArgumentException(Constants.Modules.VALIDATION_EMPTY_MODULE_NAME)


            feature.isEmpty() || feature.contains(Regex("[^a-zA-Z0-9_]")) ->
                throw IllegalArgumentException(Constants.Modules.VALIDATION_EMPTY_FEATURE_NAME)
        }
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        wizardContext.getNextButton()?.text = KotlinNewProjectWizardUIBundle.message("finish.button.text")
        return super.getPreferredFocusedComponent()
    }

    fun getData(): ModuleData =
        ModuleData(
            rootPath = wizardContext.project?.basePath.orEmpty(),
            featurePath = featurePanel.getModulePath(),
            sharedPath = sharedPanel.getModulePath(),
            projectName = projectNameField.text,
            moduleName = moduleNameField.text,
            featureName = featureNameField.text
        )
}


internal fun WizardContext.getNextButton() = try {
    AbstractWizard::class.java.getDeclaredMethod("getNextButton")
        .also { it.isAccessible = true }
        .invoke(getUserData(AbstractWizard.KEY)) as? JButton
} catch (_: Throwable) {
    null
}
