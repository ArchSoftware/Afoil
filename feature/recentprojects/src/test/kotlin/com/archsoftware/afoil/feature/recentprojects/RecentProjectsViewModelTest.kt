package com.archsoftware.afoil.feature.recentprojects

import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.SelectableAfoilProject
import com.archsoftware.afoil.core.testing.projectstore.TestAfoilProjectStore
import com.archsoftware.afoil.core.testing.repository.TestAfoilProjectDataRepository
import com.archsoftware.afoil.core.testing.repository.TestAfoilProjectRepository
import com.archsoftware.afoil.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class RecentProjectsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val projectRepository = TestAfoilProjectRepository()
    private val projectDataRepository = TestAfoilProjectDataRepository()
    private val projectStore = TestAfoilProjectStore()

    // Test data
    private val projects = buildList {
        repeat(5) {
            add(
                AfoilProject(
                    name = "My Project $it",
                    dirUri = "",
                    projectDataType = ""
                )
            )
        }
    }

    private lateinit var viewModel: RecentProjectsViewModel

    @Before
    fun setup() {
        viewModel = RecentProjectsViewModel(
            projectRepository = projectRepository,
            projectDataRepository = projectDataRepository,
            projectStore = projectStore
        )
    }

    @Test
    fun isLoadingOnStart() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.projects.collect()
        }

        assertTrue(viewModel.isLoading)

        collectJob.cancel()
    }

    @Test
    fun isNotLoadingOnEmission() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.projects.collect()
        }

        projectRepository.sendProjects(projects)
        assertFalse(viewModel.isLoading)

        collectJob.cancel()
    }

    @Test
    fun testSelection() = runTest {
        val selectedIndex = 2

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.projects.collect()
        }

        projectRepository.sendProjects(projects)

        viewModel.selectUnselect(projects[selectedIndex])
        assertTrue(viewModel.projects.value[selectedIndex].isSelected)
        assertEquals(1, viewModel.selectedCount)
        assertTrue(viewModel.showActionBar)

        viewModel.selectUnselect(projects[selectedIndex])
        assertFalse(viewModel.projects.value[selectedIndex].isSelected)
        assertEquals(0, viewModel.selectedCount)
        assertFalse(viewModel.showActionBar)

        collectJob.cancel()
    }

    @Test
    fun testOnProjectClickSelectsIfAlreadySelecting() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.projects.collect()
        }

        projectRepository.sendProjects(projects)
        viewModel.selectUnselect(projects[2])
        viewModel.onProjectClick(projects[4])
        assertTrue(viewModel.projects.value[4].isSelected)

        collectJob.cancel()
    }

    @Test
    fun testCancelSelection() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.projects.collect()
        }

        projectRepository.sendProjects(projects)
        viewModel.selectUnselect(projects[2])
        viewModel.cancelSelection()
        assertFalse(viewModel.projects.value.all { it.isSelected })
        assertEquals(0, viewModel.selectedCount)
        assertFalse(viewModel.showActionBar)

        collectJob.cancel()
    }

    @Test
    fun testDeletion() = runTest {
        val expectedResult = projects.map { project ->
            SelectableAfoilProject(project, false)
        }.drop(2)

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.projects.collect()
        }

        projectRepository.sendProjects(projects)
        viewModel.selectUnselect(projects[0])
        viewModel.selectUnselect(projects[1])
        viewModel.deleteSelected()
        assertContentEquals(expectedResult, viewModel.projects.value)

        collectJob.cancel()
    }
}