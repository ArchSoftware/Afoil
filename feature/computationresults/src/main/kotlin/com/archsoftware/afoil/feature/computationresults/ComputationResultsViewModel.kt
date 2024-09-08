package com.archsoftware.afoil.feature.computationresults

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.archsoftware.afoil.core.data.repository.ProjectNumResultRepository
import com.archsoftware.afoil.core.data.repository.ProjectPostResultRepository
import com.archsoftware.afoil.core.data.repository.ProjectRepository
import com.archsoftware.afoil.core.model.AfoilProjectPostResult
import com.archsoftware.afoil.core.model.ProjectNumResult
import com.archsoftware.afoil.core.projectstore.ProjectStore
import com.archsoftware.afoil.feature.computationresults.navigation.PROJECT_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ComputationResultsViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val projectNumResultRepository: ProjectNumResultRepository,
    private val projectPostResultRepository: ProjectPostResultRepository,
    private val projectStore: ProjectStore,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val projectId: Long = checkNotNull(savedStateHandle[PROJECT_ID_ARG])

    private val _currentPage: MutableState<ComputationResultsPage> =
        mutableStateOf(ComputationResultsPage.NumResultsPage)
    val currentPage: ComputationResultsPage
        get() = _currentPage.value

    val projectName: StateFlow<String> =
        projectRepository.getProjectById(projectId).map { it.name }
            .stateIn(
                scope = viewModelScope,
                initialValue = String(),
                started = SharingStarted.WhileSubscribed(5_000)
            )

    val projectNumResult: StateFlow<ProjectNumResult?> =
        projectNumResultRepository.getProjectNumResultByProjectId(projectId)
            .map { projectStore.readProjectNumResult(it.uri.toUri()) }
            .stateIn(
                scope = viewModelScope,
                initialValue = null,
                started = SharingStarted.WhileSubscribed(5_000)
            )

    val projectPostResults: StateFlow<List<AfoilProjectPostResult>> =
        projectPostResultRepository.getProjectPostResultsByProjectId(projectId)
            .stateIn(
                scope = viewModelScope,
                initialValue = emptyList(),
                started = SharingStarted.WhileSubscribed(5_000)
            )

    fun onPageSelected(page: ComputationResultsPage) {
        _currentPage.value = page
    }

    fun onBackPressed(): Boolean {
        val handled = _currentPage.value != ComputationResultsPage.NumResultsPage
        if (handled) {
            _currentPage.value = ComputationResultsPage.NumResultsPage
            return true
        } else {
            return false
        }
    }
}

sealed interface ComputationResultsPage {
    data object NumResultsPage : ComputationResultsPage
    data class PostResultPage(val postResult: AfoilProjectPostResult) : ComputationResultsPage
}