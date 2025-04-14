package com.widmeyertemplate.modules.presentation

import com.android.tools.idea.npw.module.ModuleDescriptionProvider
import com.android.tools.idea.npw.module.ModuleGalleryEntry
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.widmeyertemplate.modules.data.repository.ModuleRepositoryImpl
import com.widmeyertemplate.utils.Constants
import com.widmeyertemplate.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AndroidModuleDescriptionProvider : ModuleDescriptionProvider {
    private val moduleRepository = ModuleRepositoryImpl()
    private var entry: WidmeyerModuleGalleryEntry? = null

    init {
        val onSuccess: (Project) -> Unit = { project: Project ->
            CoroutineScope(Dispatchers.Main).launch {
                val data = entry?.getData() ?: return@launch

                moduleRepository.create(project = project, moduleData = data).collect {
                    when (it) {
                        is Result.Success -> Messages.showMessageDialog(
                            "",
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

        entry = WidmeyerModuleGalleryEntry(onSuccess = onSuccess)
    }

    override fun getDescriptions(project: Project): Collection<ModuleGalleryEntry> =
        if (entry == null) emptyList() else listOf(entry!!)
}