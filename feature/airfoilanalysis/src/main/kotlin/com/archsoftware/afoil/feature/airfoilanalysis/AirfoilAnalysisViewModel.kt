package com.archsoftware.afoil.feature.airfoilanalysis

import android.net.Uri
import android.util.Log
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import com.archsoftware.afoil.core.common.utils.DatAirfoilReader
import com.archsoftware.afoil.core.common.utils.isValidDoubleInput
import com.archsoftware.afoil.core.common.utils.isValidIntInput
import com.archsoftware.afoil.core.data.repository.ProjectDataRepository
import com.archsoftware.afoil.core.data.repository.ProjectRepository
import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.AfoilProjectData
import com.archsoftware.afoil.core.model.AirfoilAnalysisProjectData
import com.archsoftware.afoil.core.projectstore.ProjectStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirfoilAnalysisViewModel @Inject constructor(
    private val contentResolver: UriContentResolver,
    private val datAirfoilReader: DatAirfoilReader,
    private val projectRepository: ProjectRepository,
    private val projectDataRepository: ProjectDataRepository,
    private val projectStore: ProjectStore
) : ViewModel() {

    @VisibleForTesting
    internal val pageIndex: MutableState<Int> = mutableIntStateOf(0)

    val currentPage: AirfoilAnalysisPage by derivedStateOf {
        AirfoilAnalysisPage.entries[pageIndex.value]
    }

    val shouldShowDone: Boolean by derivedStateOf {
        pageIndex.value == AirfoilAnalysisPage.entries.lastIndex
    }

    val previousEnabled: Boolean by derivedStateOf {
        pageIndex.value > 0
    }

    private var isEmptyFieldAllowed: Boolean by mutableStateOf(true)

    private val _projectPreparingState: MutableState<ProjectPreparingState> =
        mutableStateOf(ProjectPreparingState.IDLE)
    val projectPreparingState: ProjectPreparingState
        get() = _projectPreparingState.value

    val isProjectPreparing by derivedStateOf {
        _projectPreparingState.value == ProjectPreparingState.PREPARING
    }

    /*
    Project details page variables
     */
    private val _projectName: MutableState<String> = mutableStateOf(String())
    val projectName: String
        get() = _projectName.value
    val projectNameHasError: StateFlow<Boolean> = combine(
        snapshotFlow { isEmptyFieldAllowed },
        snapshotFlow { projectName },
        projectRepository.getProjects()
    ) { isEmptyFieldAllowed, projectName, projects ->
        projects.any { it.name == projectName } || (!isEmptyFieldAllowed && projectName.isEmpty())
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    /*
    Airfoil data page variables
     */
    private var datAirfoilUri: Uri? = null
    private val _datAirfoilName: MutableState<String?> = mutableStateOf(null)
    val datAirfoilName: String?
        get() = _datAirfoilName.value

    private val _panelsNumber: MutableState<String> = mutableStateOf(String())
    val panelsNumber: String
        get() = _panelsNumber.value
    val panelsNumberHasError: Boolean
        get() = !isValidIntInput(panelsNumber, isEmptyFieldAllowed).value

    /*
    Fluid data page variables
     */
    private val _reynoldsNumber: MutableState<String> = mutableStateOf(String())
    val reynoldsNumber: String
        get() = _reynoldsNumber.value
    val reynoldsNumberHasError: Boolean
        get() = !isValidDoubleInput(reynoldsNumber).value

    private val _machNumber: MutableState<String> = mutableStateOf(String())
    val machNumber: String
        get() = _machNumber.value
    val machNumberHasError: Boolean
        get() = !isValidDoubleInput(machNumber, isEmptyFieldAllowed).value

    private val _angleOfAttack: MutableState<String> = mutableStateOf(String())
    val angleOfAttack: String
        get() = _angleOfAttack.value
    val angleOfAttackHasError: Boolean
        get() = !isValidDoubleInput(angleOfAttack, isEmptyFieldAllowed).value

    /*
    Post-processing settings page variables
     */
    private val _numberOfStreamlines: MutableState<String> = mutableStateOf(String())
    val numberOfStreamlines: String
        get() = _numberOfStreamlines.value
    val numberOfStreamlinesHasError: Boolean
        get() = !isValidIntInput(numberOfStreamlines, isEmptyFieldAllowed).value

    private val _streamlinesRefiningLevel: MutableState<Float> = mutableFloatStateOf(0f)
    val streamlinesRefiningLevel: Float
        get() = _streamlinesRefiningLevel.value

    private val _pressureContoursGridSize: MutableState<String> = mutableStateOf(String())
    val pressureContoursGridSize: String
        get() = _pressureContoursGridSize.value
    val pressureContoursGridSizeHasError: Boolean
        get() = !isValidIntInput(pressureContoursGridSize, isEmptyFieldAllowed).value

    private val _pressureContoursRefiningLevel: MutableState<Float> = mutableFloatStateOf(0f)
    val pressureContoursRefiningLevel: Float
        get() = _pressureContoursRefiningLevel.value

    /*
    Project details page methods
     */
    fun onProjectNameChange(value: String) {
        _projectName.value = value
    }

    /*
    Airfoil data page methods
     */
    fun onDatAirfoilSelected(uri: Uri?) {
        datAirfoilUri = uri

        viewModelScope.launch {
            var name: String? = null
            if (uri != null) {
                try {
                    name = datAirfoilReader.readName(uri)
                } catch (e: Exception) {
                    Log.e("AirfoilAnalysisViewModel", "Failed to read airfoil data", e)
                }
            }

            _datAirfoilName.value = name
        }
    }

    fun onPanelsNumberChange(value: String) {
        _panelsNumber.value = value
    }

    /*
    Fluid data page methods
     */
    fun onReynoldsNumberChange(value: String) {
        _reynoldsNumber.value = value
    }

    fun onMachNumberChange(value: String) {
        _machNumber.value = value
    }

    fun onAngleOfAttackChange(value: String) {
        _angleOfAttack.value = value
    }

    /*
    Post-processing settings page methods
     */
    fun onNumberOfStreamlinesChange(value: String) {
        _numberOfStreamlines.value = value
    }

    fun onStreamlinesRefiningLevelChange(value: Float) {
        _streamlinesRefiningLevel.value = value
    }

    fun onPressureContoursGridSizeChange(value: String) {
        _pressureContoursGridSize.value = value
    }

    fun onPressureContoursRefiningLevelChange(value: Float) {
        _pressureContoursRefiningLevel.value = value
    }

    /*
    Navigation methods
     */
    fun onNextClick() {
        if (!checkDataValidity()) return

        pageIndex.value += 1
    }

    fun onDone() {
        if (!checkDataValidity()) return
        val datAirfoilUri = datAirfoilUri ?: return

        viewModelScope.launch {
            _projectPreparingState.value = ProjectPreparingState.PREPARING
            val datAirfoilDisplayName = contentResolver.getDisplayName(datAirfoilUri) ?: return@launch
            val projectData = AirfoilAnalysisProjectData(
                datAirfoilDisplayName = datAirfoilDisplayName,
                panelsNumber = panelsNumber.toInt(),
                reynoldsNumber = reynoldsNumber.toDoubleOrNull(),
                machNumber = machNumber.toDouble(),
                angleOfAttack = angleOfAttack.toDouble(),
                numberOfStreamlines = numberOfStreamlines.toInt(),
                streamlinesRefiningLevel = streamlinesRefiningLevel,
                pressureContoursGridSize = pressureContoursGridSize.toInt(),
                pressureContoursRefiningLevel = pressureContoursRefiningLevel
            )
            val projectDirUri = projectStore.createProjectDir(projectName) ?: return@launch
            projectStore.copyToProjectDir(projectDirUri, datAirfoilUri)
            val projectDataFileUri =
                projectStore.writeProjectData(projectDirUri, projectData) ?: return@launch
            val project = AfoilProject(
                name = projectName,
                dirUri = projectDirUri.toString(),
                projectDataType = AirfoilAnalysisProjectData::class.java.name
            )
            val projectId = projectRepository.insertProject(project)
            val data = AfoilProjectData(
                uri = projectDataFileUri.toString(),
                projectOwnerId = projectId
            )
            projectDataRepository.insertProjectData(data)
            _projectPreparingState.value = ProjectPreparingState.DONE
        }
    }

    fun onPreviousClick() {
        pageIndex.value = (pageIndex.value - 1).coerceAtLeast(0)
    }

    fun onBackPressed(): Boolean {
        val handled = pageIndex.value > 0
        if (handled) {
            onPreviousClick()
            return true
        } else {
            return false
        }
    }

    private fun checkDataValidity(): Boolean {
        when (currentPage) {
            AirfoilAnalysisPage.PROJECT_DETAILS -> {
                if (projectName.isEmpty()) {
                    isEmptyFieldAllowed = false
                    return false
                } else {
                    return !projectNameHasError.value
                }
            }
            AirfoilAnalysisPage.AIRFOIL_DATA -> {
                if (panelsNumber.isEmpty()) {
                    isEmptyFieldAllowed = false
                    return false
                } else {
                    return !panelsNumberHasError && datAirfoilName != null
                }
            }
            AirfoilAnalysisPage.FLUID_DATA -> {
                if (machNumber.isEmpty() || angleOfAttack.isEmpty()) {
                    isEmptyFieldAllowed = false
                    return false
                } else {
                    return !reynoldsNumberHasError && !machNumberHasError && !angleOfAttackHasError
                }

            }
            AirfoilAnalysisPage.POST_PROCESSING_SETTINGS -> {
                if (numberOfStreamlines.isEmpty() || pressureContoursGridSize.isEmpty()) {
                    isEmptyFieldAllowed = false
                    return false
                } else {
                    return !numberOfStreamlinesHasError && !pressureContoursGridSizeHasError
                }
            }
        }
    }
}

enum class AirfoilAnalysisPage(@StringRes val titleId: Int) {
    PROJECT_DETAILS(R.string.feature_airfoilanalysis_projectdetailspage_title),
    AIRFOIL_DATA(R.string.feature_airfoilanalysis_airfoildatapage_title),
    FLUID_DATA(R.string.feature_airfoilanalysis_fluiddatapage_title),
    POST_PROCESSING_SETTINGS(R.string.feature_airfoilanalysis_postprocessingsettingspage_title)
}

enum class ProjectPreparingState {
    IDLE,
    PREPARING,
    DONE
}