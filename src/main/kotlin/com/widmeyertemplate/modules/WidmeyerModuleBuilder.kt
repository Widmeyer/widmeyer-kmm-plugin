package com.widmeyertemplate.modules

import WidmeyerModuleWizardStep
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ModifiableRootModel

class WidmeyerModuleBuilder : ModuleBuilder() {
    override fun getModuleType(): ModuleType<*> = WidmeyerModuleType.getInstance()

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
    }

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?) =
        WidmeyerModuleWizardStep()
}
