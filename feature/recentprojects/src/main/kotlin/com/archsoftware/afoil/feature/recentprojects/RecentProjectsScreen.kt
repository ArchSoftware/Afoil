package com.archsoftware.afoil.feature.recentprojects

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.archsoftware.afoil.core.designsystem.component.SelectionAppBar
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.AirfoilAnalysisProjectData
import com.archsoftware.afoil.core.model.SelectableAfoilProject
import com.archsoftware.afoil.core.ui.AfoilProjectItem

@Composable
fun RecentProjectsScreen(
    onNavigateUp: () -> Unit,
    onProjectClick: (projectName: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecentProjectsViewModel = hiltViewModel()
) {
    val recentProjects by viewModel.projects.collectAsStateWithLifecycle()

    RecentProjectsScreen(
        isLoading = viewModel.isLoading,
        selectedCount = viewModel.selectedCount,
        showActionBar = viewModel.showActionBar,
        recentProjects = recentProjects,
        onProjectClick = { project ->
            if (!viewModel.onProjectClick(project)) {
                onProjectClick(project.name)
            }
        },
        onNavigateUp = onNavigateUp,
        onProjectLongClick = viewModel::selectUnselect,
        onCancelIconClick = viewModel::cancelSelection,
        onDeleteIconClick = viewModel::deleteSelected,
        modifier = modifier
    )
}

@Composable
internal fun RecentProjectsScreen(
    isLoading: Boolean,
    selectedCount: Int,
    showActionBar: Boolean,
    recentProjects: List<SelectableAfoilProject>,
    onProjectClick: (project: AfoilProject) -> Unit,
    onNavigateUp: () -> Unit,
    onProjectLongClick: (project: AfoilProject) -> Unit,
    onCancelIconClick: () -> Unit,
    onDeleteIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            SelectionAppBar(
                title = stringResource(id = R.string.feature_recentprojects_title),
                navIcon = Icons.AutoMirrored.Filled.ArrowBack,
                navIconContentDescription = stringResource(
                    id = R.string.feature_recentprojects_navigateup_content_desc
                ),
                selectedCount = selectedCount,
                showActionBar = showActionBar,
                onNavIconClick = onNavigateUp,
                onCancelIconClick = onCancelIconClick,
                actions = {
                    IconButton(onClick = onDeleteIconClick) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(
                                id = R.string.feature_recentprojects_delete_content_desc
                            )
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { padding ->
        AnimatedContent(
            targetState = isLoading,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding),
            label = "recentProjectsAnimation"
        ) { targetState ->
            if (targetState) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()

                ) {
                    CircularProgressIndicator()
                    Text(
                        text = stringResource(id = R.string.feature_recentprojects_loading_text),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                if (recentProjects.isNotEmpty()) {
                    LazyColumn {
                        items(
                            items = recentProjects,
                            key = { project -> project.afoilProject.name }
                        ) { project ->
                            AfoilProjectItem(
                                name = project.afoilProject.name,
                                icon = projectIcons.getOrDefault(
                                    key = project.afoilProject.projectDataType,
                                    defaultValue = Icons.Filled.Folder
                                ),
                                isSelected = project.isSelected,
                                onClick = { onProjectClick(project.afoilProject) },
                                onLongClick = { onProjectLongClick(project.afoilProject) }
                            )
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()

                    ) {
                        Text(
                            text = stringResource(id = R.string.feature_recentprojects_no_project_found),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

private val projectIcons: Map<String, ImageVector> = mapOf(
    AirfoilAnalysisProjectData::class.java.name to Icons.Filled.QueryStats
)

@Preview(device = Devices.PIXEL_7)
@Composable
private fun RecentProjectsScreenPreview() {
    AfoilTheme {
        RecentProjectsScreen(
            isLoading = false,
            selectedCount = 0,
            showActionBar = false,
            recentProjects = buildList {
                repeat(10) {
                    add(
                        SelectableAfoilProject(
                            afoilProject = AfoilProject(
                                name = "My Project $it",
                                projectDataType = ""
                            ),
                            isSelected = false
                        )
                    )
                }
            },
            onProjectClick = {},
            onNavigateUp = {},
            onProjectLongClick = {},
            onCancelIconClick = {},
            onDeleteIconClick = {}
        )
    }
}