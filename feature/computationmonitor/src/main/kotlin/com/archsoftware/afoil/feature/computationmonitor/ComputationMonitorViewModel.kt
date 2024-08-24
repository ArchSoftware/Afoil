package com.archsoftware.afoil.feature.computationmonitor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.archsoftware.afoil.computation.manager.ComputationManager
import com.archsoftware.afoil.core.model.ComputationLog
import com.archsoftware.afoil.feature.computationmonitor.navigation.PROJECT_NAME_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ComputationMonitorViewModel @Inject constructor(
    private val computationManager: ComputationManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val projectName: String = checkNotNull(savedStateHandle[PROJECT_NAME_ARG])

    val state: StateFlow<ComputationManager.State> =
        computationManager.getComputationState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ComputationManager.State.IDLE
            )

    val logs: StateFlow<List<ComputationLog>> =
        computationManager.getComputationLogs()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val progress: StateFlow<Float> =
        computationManager.getComputationProgress()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0f
            )

    val showProgressBar: StateFlow<Boolean> =
        computationManager.getComputationState().map { state ->
            state == ComputationManager.State.RUNNING
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )
}