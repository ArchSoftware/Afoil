package com.archsoftware.afoil.feature.airfoilanalysis

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.archsoftware.afoil.core.designsystem.component.NavCenterAlignedAppBar
import com.archsoftware.afoil.core.designsystem.component.NextPrevBottomBar
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.core.ui.ProjectPreparingDialog
import com.archsoftware.afoil.feature.airfoilanalysis.page.AirfoilDataPage
import com.archsoftware.afoil.feature.airfoilanalysis.page.FluidDataPage
import com.archsoftware.afoil.feature.airfoilanalysis.page.PostProcessingSettingsPage
import com.archsoftware.afoil.feature.airfoilanalysis.page.ProjectDetailsPage

private const val CONTENT_ANIMATION_DURATION = 300

@Composable
fun AirfoilAnalysisScreen(
    onNavigateUp: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AirfoilAnalysisViewModel = hiltViewModel()
) {
    var showProjectPreparingDialog by remember { mutableStateOf(false) }
    val projectNameHasError by viewModel.projectNameHasError.collectAsStateWithLifecycle()

    BackHandler {
        if (!viewModel.onBackPressed()) onNavigateUp()
    }

    LaunchedEffect(viewModel.projectPreparingState) {
        if (viewModel.projectPreparingState == ProjectPreparingState.DONE) {
            showProjectPreparingDialog = false
            onDone()
        } else if (viewModel.projectPreparingState == ProjectPreparingState.PREPARING) {
            showProjectPreparingDialog = true
        }
    }

    AirfoilAnalysisScreen(
        showProjectPreparingDialog = showProjectPreparingDialog,
        currentPage = viewModel.currentPage,
        projectName = viewModel.projectName,
        shouldShowDone = viewModel.shouldShowDone,
        previousEnabled = viewModel.previousEnabled,
        onPreviousClick = viewModel::onPreviousClick,
        onNextClick = viewModel::onNextClick,
        onNavigateUp = onNavigateUp,
        onDone = viewModel::onDone,
        onProjectPreparingDialogDismiss = { showProjectPreparingDialog = false },
        modifier = modifier
    ) { targetState ->
        when (targetState) {
            AirfoilAnalysisPage.PROJECT_DETAILS -> {
                ProjectDetailsPage(
                    projectName = viewModel.projectName,
                    projectNameHasError = projectNameHasError,
                    onProjectNameChange = viewModel::onProjectNameChange,
                )
            }
            AirfoilAnalysisPage.AIRFOIL_DATA -> {
                AirfoilDataPage(
                    datAirfoilName = viewModel.datAirfoilName,
                    panelsNumber = viewModel.panelsNumber,
                    panelsNumberHasError = viewModel.panelsNumberHasError,
                    onDatAirfoilSelected = viewModel::onDatAirfoilSelected,
                    onPanelsNumberChange = viewModel::onPanelsNumberChange
                )
            }
            AirfoilAnalysisPage.FLUID_DATA -> {
                FluidDataPage(
                    reynoldsNumber = viewModel.reynoldsNumber,
                    machNumber = viewModel.machNumber,
                    angleOfAttack = viewModel.angleOfAttack,
                    onReynoldsNumberChange = viewModel::onReynoldsNumberChange,
                    onMachNumberChange = viewModel::onMachNumberChange,
                    onAngleOfAttackChange = viewModel::onAngleOfAttackChange,
                    reynoldsNumberHasError = viewModel.reynoldsNumberHasError,
                    machNumberHasError = viewModel.machNumberHasError,
                    angleOfAttackHasError = viewModel.angleOfAttackHasError
                )
            }
            AirfoilAnalysisPage.POST_PROCESSING_SETTINGS -> {
                PostProcessingSettingsPage(
                    numberOfStreamlines = viewModel.numberOfStreamlines,
                    streamlinesRefiningLevel = viewModel.streamlinesRefiningLevel,
                    pressureContoursGridSize = viewModel.pressureContoursGridSize,
                    pressureContoursRefiningLevel = viewModel.pressureContoursRefiningLevel,
                    numberOfStreamlinesHasError = viewModel.numberOfStreamlinesHasError,
                    pressureContoursGridSizeHasError = viewModel.pressureContoursGridSizeHasError,
                    onNumberOfStreamlinesChange = viewModel::onNumberOfStreamlinesChange,
                    onStreamlinesRefiningLevelChange = viewModel::onStreamlinesRefiningLevelChange,
                    onPressureContoursGridSizeChange = viewModel::onPressureContoursGridSizeChange,
                    onPressureContoursRefiningLevelChange = viewModel::onPressureContoursRefiningLevelChange
                )
            }
        }
    }
}

@Composable
internal fun AirfoilAnalysisScreen(
    showProjectPreparingDialog: Boolean,
    currentPage: AirfoilAnalysisPage,
    projectName: String,
    shouldShowDone: Boolean,
    previousEnabled: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onNavigateUp: () -> Unit,
    onDone: () -> Unit,
    onProjectPreparingDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (currentPage: AirfoilAnalysisPage) -> Unit
) {
    if (showProjectPreparingDialog) {
        ProjectPreparingDialog(
            projectName = projectName,
            onDismissRequest = onProjectPreparingDialogDismiss,
            modifier = Modifier.testTag("projectPreparingDialog")
        )
    }

    Scaffold(
        topBar = {
            NavCenterAlignedAppBar(
                title = stringResource(id = R.string.feature_airfoilanalysis_title),
                navIcon = Icons.AutoMirrored.Filled.ArrowBack,
                navIconContentDescription = stringResource(id = R.string.feature_airfoilanalysis_navigateup_content_desc),
                onNavIconClick = onNavigateUp
            )
        },
        bottomBar = {
            NextPrevBottomBar(
                shouldShowDone = shouldShowDone,
                previousEnabled = previousEnabled,
                isLoading = showProjectPreparingDialog,
                onPreviousClick = onPreviousClick,
                onNextClick = onNextClick,
                onDone = onDone
            )
        },
        modifier = modifier
    ) { padding ->
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                val animationSpec: TweenSpec<IntOffset> = tween(CONTENT_ANIMATION_DURATION)
                val direction = getSlideDirection(initialState, targetState)

                slideIntoContainer(
                    towards = direction,
                    animationSpec = animationSpec
                ) togetherWith slideOutOfContainer(
                    towards = direction,
                    animationSpec = animationSpec
                )
            },
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding),
            label = "airfoilAnalysisPageAnimation"
        ) { targetState ->
            content(targetState)
        }
    }
}

private fun getSlideDirection(
    initialState: AirfoilAnalysisPage,
    targetState: AirfoilAnalysisPage
) = if (initialState.ordinal > targetState.ordinal) {
    AnimatedContentTransitionScope.SlideDirection.Right
} else {
    AnimatedContentTransitionScope.SlideDirection.Left
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun AirfoilAnalysisScreenPreview() {
    AfoilTheme {
        AirfoilAnalysisScreen(
            onNavigateUp = {},
            onDone = {}
        )
    }
}