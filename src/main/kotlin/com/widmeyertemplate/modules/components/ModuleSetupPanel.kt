package com.widmeyertemplate.modules.components

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.util.ui.JBUI
import com.widmeyertemplate.modules.model.TypeModel
import org.jdesktop.swingx.VerticalLayout
import java.awt.Dimension
import java.io.File
import javax.swing.*

class ModuleSetupPanel(private val typeModel: TypeModel) : JComponent() {
    private val moduleNameField: JTextField
    private val featureNameField: JTextField
    private val pathField: TextFieldWithBrowseButton
    private val createModuleCheckbox: JCheckBox
    private val rootPath = System.getProperty("user.dir")

    init {
        layout = VerticalLayout()
        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        createModuleCheckbox = JCheckBox(getCheckboxLabel()).apply {
            toolTipText = "Выберите, если хотите создать этот тип модуля"
            addActionListener { toggleVisibility(this.isSelected) }
        }
        add(createModuleCheckbox)

        add(JLabel("Введите название модуля (MODULE_NAME):"))
        moduleNameField = JTextField().apply {
            preferredSize = Dimension(480, 32)
            maximumSize = preferredSize
        }
        add(moduleNameField)

        add(JLabel("Введите имя фичи (FEATURE_NAME):"))
        featureNameField = JTextField().apply {
            preferredSize = Dimension(480, 32)
            maximumSize = preferredSize
            margin = JBUI.insetsLeft(240)
        }
        add(featureNameField)

        add(JLabel("Выберите путь для создания модуля:"))
        pathField = TextFieldWithBrowseButton().apply {
            preferredSize = Dimension(480, 32)
            maximumSize = preferredSize
            text = getDefaultPath()
            addBrowseFolderListener(
                "Выберите директорию",
                "Выберите путь для модуля",
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
            TypeModel.FEATURE -> "Создать Feature модуль"
            TypeModel.SHARED -> "Создать Shared Feature модуль"
        }
    }

    private fun toggleVisibility(isVisible: Boolean) {
        moduleNameField.isEnabled = isVisible
        featureNameField.isEnabled = isVisible
        pathField.isEnabled = isVisible
    }

    fun getModuleName() = moduleNameField.text.trim().lowercase()
    fun getFeatureName() = featureNameField.text.trim().capitalize()
    fun getModulePath() = pathField.text.trim()


    fun validateInput(): Boolean {
        val moduleName = getModuleName()
        val featureName = getFeatureName()
        val modulePath = getModulePath()

        return when {
            moduleName.isEmpty() || moduleName.contains(Regex("[^a-z0-9_]")) -> {
                JOptionPane.showMessageDialog(
                    this,
                    "MODULE_NAME должен быть в нижнем регистре без спецсимволов.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
                )
                false
            }

            featureName.isEmpty() -> {
                JOptionPane.showMessageDialog(
                    this,
                    "FEATURE_NAME не может быть пустым.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
                )
                false
            }

            modulePath.isEmpty() || !File(modulePath).exists() -> {
                JOptionPane.showMessageDialog(
                    this,
                    "Путь для создания модуля не существует.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
                )
                false
            }

            else -> true
        }
    }

    override fun isEnabled() = moduleNameField.isEnabled
}