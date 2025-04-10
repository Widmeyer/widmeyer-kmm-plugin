import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.widmeyertemplate.Constants
import com.widmeyertemplate.feature.ModuleSetupPanel
import com.widmeyertemplate.model.TypeModel
import org.jdesktop.swingx.VerticalLayout
import java.awt.Component
import java.awt.Font
import javax.swing.*


class FeatureModuleWizardStep() : ModuleWizardStep() {
    private val panel: JPanel
    private val featurePanel = ModuleSetupPanel(TypeModel.FEATURE)
    private val sharedPanel = ModuleSetupPanel(TypeModel.SHARED)

    init {
        panel = JPanel().apply {
            layout = VerticalLayout()
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

            add(JLabel("Настройки Feature модуля").apply {
                font = Font("Arial", Font.BOLD, 14)
            })

            add(featurePanel)
            add(sharedPanel)
        }
    }

    override fun getComponent() = panel

    override fun updateDataModel() {
        if (!featurePanel.isEnabled() && !sharedPanel.isEnabled()) {
            throw IllegalArgumentException("Выберите хотя бы один тип модуля")
        }
    }

    override fun onWizardFinished() {
        try {
            updateDataModel()
        } catch (ex: IllegalArgumentException) {
            JOptionPane.showMessageDialog(panel, ex.message, "Ошибка", JOptionPane.ERROR_MESSAGE)
        }
    }
}