package com.widmeyertemplate.modules.presentation

import com.android.tools.idea.npw.module.ConfigureModuleStep
import com.android.tools.idea.npw.toWizardFormFactor
import com.intellij.openapi.project.Project
import com.widmeyertemplate.modules.domain.model.ModuleData
import com.widmeyertemplate.modules.presentation.components.ModuleSetupPanel
import com.widmeyertemplate.utils.Constants
import com.widmeyertemplate.utils.TypeModel
import org.jdesktop.swingx.VerticalLayout
import java.awt.Dimension
import java.awt.Font
import javax.swing.*

class WidmeyerModuleAndroidStep(
    basePackage: String,
    minSdkLevel: Int,
    title: String,
    private val model: AndroidModuleModel,
    private val onSuccess: (Project) -> Unit,
) : ConfigureModuleStep<AndroidModuleModel>(
    model,
    model.formFactor.get().toWizardFormFactor(),
    minSdkLevel = minSdkLevel,
    basePackage = basePackage,
    title = title
) {
    private val panel: JPanel
    private val featurePanel =
        ModuleSetupPanel(basePath = model.project.basePath.orEmpty(), typeModel = TypeModel.FEATURE)
    private val sharedPanel =
        ModuleSetupPanel(basePath = model.project.basePath.orEmpty(), typeModel = TypeModel.SHARED)
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
    override fun createMainPanel(): JPanel = panel
    override fun onProceeding() {
        try {
            validateOrThrow()
            onSuccess(model.project)
        } catch (ex: IllegalArgumentException) {
            JOptionPane.showMessageDialog(panel, ex.message, Constants.Common.ERROR, JOptionPane.ERROR_MESSAGE)
        }
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


    fun getData(): ModuleData =
        ModuleData(
            rootPath = model.project.basePath.orEmpty(),
            featurePath = featurePanel.getModulePath(),
            sharedPath = sharedPanel.getModulePath(),
            projectName = projectNameField.text,
            moduleName = moduleNameField.text,
            featureName = featureNameField.text
        )
}