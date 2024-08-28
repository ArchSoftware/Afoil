package com.archsoftware.afoil.feature.recentprojects

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.archsoftware.afoil.core.data.repository.ProjectDataRepository
import com.archsoftware.afoil.core.data.repository.ProjectRepository
import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.SelectableAfoilProject
import com.archsoftware.afoil.core.projectstore.ProjectStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentProjectsViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val projectDataRepository: ProjectDataRepository,
    private val projectStore: ProjectStore
) : ViewModel() {
    private val recentProjects: Flow<List<AfoilProject>> = projectRepository.getProjects()

    private val selectedProjects: MutableStateFlow<List<AfoilProject>> = MutableStateFlow(
        mutableListOf()
    )

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading.value

    val projects: StateFlow<List<SelectableAfoilProject>> =
        selectedProjects.combine(recentProjects) { selectedProjects, recentProjects ->
            _isLoading.value = false
            recentProjects.map { recentProject ->
                SelectableAfoilProject(recentProject, selectedProjects.contains(recentProject))
            }
        }
            .onStart {
                _isLoading.value = true
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val selectedCount: Int
        get() = derivedStateOf { selectedProjects.value.size }.value

    val showActionBar: Boolean
        get() = derivedStateOf { selectedCount > 0 }.value

    fun onProjectClick(project: AfoilProject): Boolean {
        if (selectedCount > 0) {
            selectUnselect(project)
            return true
        }
        return false
    }

    fun selectUnselect(afoilProject: AfoilProject) {
        if (!selectedProjects.value.contains(afoilProject)) {
            selectedProjects.value += afoilProject
        } else {
            selectedProjects.value -= afoilProject
        }
    }

    fun cancelSelection() { selectedProjects.value = emptyList() }

    fun deleteSelected() {
        val selectedProjects = selectedProjects.value
        cancelSelection()
        viewModelScope.launch {
            projectRepository.deleteProjects(selectedProjects)
            selectedProjects.forEach { projectDataRepository.deleteProjectDataByProjectId(it.id) }
            selectedProjects.forEach { projectStore.deleteProject(it.dirUri.toUri()) }
        }
    }
}