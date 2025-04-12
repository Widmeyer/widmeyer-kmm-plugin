package com.widmeyertemplate.modules.presentation.components

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.widmeyertemplate.utils.Constants
import com.widmeyertemplate.modules.domain.model.TypeModel
import org.jdesktop.swingx.VerticalLayout
import java.awt.Dimension
import javax.swing.*

class ModuleSetupPanel(private val typeModel: TypeModel) : JComponent() {
    private val pathField: TextFieldWithBrowseButton
    private val createModuleCheckbox: JCheckBox
    private val rootPath = System.getProperty("user.dir")

    init {
        layout = VerticalLayout()
        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        createModuleCheckbox = JCheckBox(getCheckboxLabel()).apply {
            toolTipText = Constants.Modules.MODULE_SELECT
            addActionListener { toggleVisibility(this.isSelected) }
        }
        add(createModuleCheckbox)

        add(JLabel(Constants.Modules.MODULE_SELECT_PATH_LABEL))
        pathField = TextFieldWithBrowseButton().apply {
            preferredSize = Dimension(480, 32)
            maximumSize = preferredSize
            text = getDefaultPath()
            addBrowseFolderListener(
                Constants.Modules.MODULE_SELECT_TITLE,
                Constants.Modules.MODULE_SELECT_PATH,
                null,
                FileChooserDescriptorFactory.createSingleFileDescriptor()
            )
        }
        add(pathField)

        toggleVisibility(false)
    }

    private fun getDefaultPath(): String {
        return when (typeModel) {
            TypeModel.FEATURE -> "$rootPath/androidApp/features/screen"
            TypeModel.SHARED -> "$rootPath/shared/features"
        }
    }

    private fun getCheckboxLabel(): String {
        return when (typeModel) {
            TypeModel.FEATURE -> Constants.Modules.CREATE_FEATURE_MODULE
            TypeModel.SHARED -> Constants.Modules.CREATE_SHARED_FEATURE_MODULE
        }
    }

    private fun toggleVisibility(isVisible: Boolean) {
        pathField.isEnabled = isVisible
    }

    fun getModulePath() = pathField.text.trim()

    override fun isEnabled() = createModuleCheckbox.isSelected
}