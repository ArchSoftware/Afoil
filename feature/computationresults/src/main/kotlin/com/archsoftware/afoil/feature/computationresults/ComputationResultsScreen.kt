package com.archsoftware.afoil.feature.computationresults

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.archsoftware.afoil.core.designsystem.component.NavCenterAlignedAppBar
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.core.model.AfoilProjectPostResult
import com.archsoftware.afoil.core.model.AirfoilAnalysisNumResult
import com.archsoftware.afoil.feature.computationresults.page.AirfoilAnalysisNumResultsPage
import com.archsoftware.afoil.feature.computationresults.page.PostResultPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.archsoftware.afoil.core.common.R as commonR

@Composable
fun ComputationResultsScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ComputationResultsViewModel = hiltViewModel()
) {
    val projectName by viewModel.projectName.collectAsStateWithLifecycle()
    val numResult by viewModel.projectNumResult.collectAsStateWithLifecycle()
    val postResults by viewModel.projectPostResults.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        if (drawerState.isOpen) {
            coroutineScope.launch {
                drawerState.close()
            }
        } else {
            if (!viewModel.onBackPressed()) onNavigateUp()
        }
    }

    ComputationResultsScreen(
        currentPage = viewModel.currentPage,
        projectName = projectName,
        projectPostResults = postResults,
        onPageSelected = viewModel::onPageSelected,
        drawerState = drawerState,
        coroutineScope = coroutineScope,
        modifier = modifier
    ) { currentPage ->
        when (currentPage) {
            is ComputationResultsPage.NumResultsPage -> {
                numResult?.let {
                    when (it) {
                        is AirfoilAnalysisNumResult -> {
                            AirfoilAnalysisNumResultsPage(
                                numResult = it,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
            is ComputationResultsPage.PostResultPage -> {
                PostResultPage(
                    postResult = currentPage.postResult,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
internal fun ComputationResultsScreen(
    currentPage: ComputationResultsPage,
    projectName: String,
    projectPostResults: List<AfoilProjectPostResult>,
    onPageSelected: (page: ComputationResultsPage) -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable (currentPage: ComputationResultsPage) -> Unit
) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = projectName,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Numbers,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(
                                id = R.string.feature_computationresults_numericalresultspage_title
                            )
                        )
                    },
                    selected = currentPage == ComputationResultsPage.NumResultsPage,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }.invokeOnCompletion {
                            onPageSelected(ComputationResultsPage.NumResultsPage)
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                HorizontalDivider(modifier = Modifier.padding(top = 16.dp))
                Text(
                    text = stringResource(id = R.string.feature_computationresults_postprocessing_results),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(16.dp)
                )
                projectPostResults.forEach { postResult ->
                    NavigationDrawerItem(
                        label = { Text(text = stringResource(id = postResult.nameId)) },
                        selected = currentPage == ComputationResultsPage.PostResultPage(postResult),
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }.invokeOnCompletion {
                                onPageSelected(ComputationResultsPage.PostResultPage(postResult))
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        drawerState = drawerState,
        modifier = modifier
    ) {
        Scaffold(
            topBar = {
                NavCenterAlignedAppBar(
                    title = stringResource(
                        id = R.string.feature_computationresults_title,
                        projectName
                    ),
                    navIcon = Icons.Filled.Menu,
                    navIconContentDescription = stringResource(
                        id = R.string.feature_computationresults_openclose_drawer_content_desc
                    ),
                    onNavIconClick = {
                        coroutineScope.launch {
                            if (drawerState.isOpen) {
                                drawerState.close()
                            } else {
                                drawerState.open()
                            }
                        }
                    }
                )
            }
        ) { padding ->
            Crossfade(
                targetState = currentPage,
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding),
                label = "computationResultsPageAnimation"
            ) { targetState ->
                content(targetState)
            }
        }
    }
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun ComputationResultsScreenPreview() {
    AfoilTheme {
        ComputationResultsScreen(
            currentPage = ComputationResultsPage.NumResultsPage,
            projectName = "My Project",
            projectPostResults = listOf(
                AfoilProjectPostResult(
                    id = 0,
                    nameId = commonR.string.streamlines_plot,
                    uri = "",
                    projectOwnerId = 0
                )
            ),
            onPageSelected = {}
        ) {}
    }
}