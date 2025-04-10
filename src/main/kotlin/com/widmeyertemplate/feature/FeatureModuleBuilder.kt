package com.widmeyertemplate.feature

import FeatureModuleWizardStep
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ModifiableRootModel

class FeatureModuleBuilder : ModuleBuilder() {
    override fun getModuleType(): ModuleType<*> = FeatureModuleType.getInstance()

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        // Твоя настройка проекта тут
    }

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?) =
        FeatureModuleWizardStep()
}
