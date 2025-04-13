package com.widmeyertemplate.resource.presentation
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.widmeyertemplate.resource.data.repository.ResourceRepositoryImpl
import com.widmeyertemplate.resource.domain.repository.ResourceRepository
import com.widmeyertemplate.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.widmeyertemplate.utils.Result

class ResourceSync : AnAction(Constants.Resources.ACTION_BUTTON) {
    private val repository: ResourceRepository = ResourceRepositoryImpl()

    override fun actionPerformed(anActionEvent: AnActionEvent) {
        observeDialog(anActionEvent)
    }

    private fun observeDialog(anActionEvent: AnActionEvent) {
        anActionEvent.project?.let { project ->
            CoroutineScope(Dispatchers.Main).launch {
                repository.update(project = project).collect {
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
}