package com.archsoftware.afoil.feature.airfoilanalysis

import androidx.compose.runtime.snapshots.Snapshot
import com.archsoftware.afoil.core.common.utils.DatAirfoilReader
import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.AirfoilAnalysisProjectData
import com.archsoftware.afoil.core.testing.contentresolver.TestAfoilContentResolver
import com.archsoftware.afoil.core.testing.projectstore.TestAfoilProjectStore
import com.archsoftware.afoil.core.testing.repository.TestAfoilProjectRepository
import com.archsoftware.afoil.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AirfoilAnalysisViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val afoilContentResolver = TestAfoilContentResolver()
    private val projectRepository = TestAfoilProjectRepository()

    private lateinit var viewModel: AirfoilAnalysisViewModel

    // Test data
    private val invalidValue = "invalid"
    private val projects = listOf(
        AfoilProject(
            name = "Project 1",
            projectDataType = AirfoilAnalysisProjectData::class.java.name)
    )
    private lateinit var datAirfoilFile: File
    private val panelsNumber = "100"
    private val reynoldsNumber = "20e3"
    private val machNumber = "0.3"
    private val angleOfAttack = "5"
    private val numberOfStreamlines = "50"
    private val pressureContoursGridSize = "50"

    @Before
    fun setup() {
        datAirfoilFile = tmpFolder.newFile("naca0012.dat")
        datAirfoilFile.writeText("NACA 0012")

        viewModel = AirfoilAnalysisViewModel(
            datAirfoilReader = DatAirfoilReader(
                contentResolver = afoilContentResolver,
                ioDispatcher = StandardTestDispatcher()
            ),
            projectRepository = projectRepository,
            projectStore = TestAfoilProjectStore(),
        )
    }

    @Test
    fun previousButtonDisabledOnFirstPage() {
        viewModel.pageIndex.value = 0
        
        assert(!viewModel.previousEnabled)
    }

    @Test
    fun doneButtonDisplayedOnLastPage() {
        viewModel.pageIndex.value = 3

        assert(viewModel.shouldShowDone)
    }

    @Test
    fun previousButton() {
        viewModel.pageIndex.value = 1

        viewModel.onPreviousClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.entries.first())
    }

    @Test
    fun onBackPressed() {
        viewModel.pageIndex.value = 1

        assert(viewModel.onBackPressed())
        assert(viewModel.pageIndex.value == 0)
        assert(!viewModel.onBackPressed())
    }

    @Test
    fun projectDetailsPageEmptyFieldAllowedOnlyBeforeNextClick() = runTest {
        viewModel.pageIndex.value = 0

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.projectNameHasError.collect()
        }

        projectRepository.sendProjects(projects)
        assert(!viewModel.projectNameHasError.value)
        // Simulate snapshot environment
        Snapshot.withMutableSnapshot {
            viewModel.onNextClick()
        }
        assert(viewModel.projectNameHasError.value)

        collectJob.cancel()
    }

    @Test
    fun projectDetailsPageStillDisplayedIfDataIsInvalid() = runTest {
        viewModel.pageIndex.value = 0

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.projectNameHasError.collect()
        }

        projectRepository.sendProjects(projects)
        // Simulate snapshot environment
        Snapshot.withMutableSnapshot {
            viewModel.onProjectNameChange(projects.first().name)
        }
        assert(viewModel.projectNameHasError.value)

        // Simulate snapshot environment
        Snapshot.withMutableSnapshot {
            viewModel.onProjectNameChange("Project 2")
        }
        assert(!viewModel.projectNameHasError.value)

        collectJob.cancel()
    }

    @Test
    fun airfoilDataPageEmptyFieldAllowedOnlyBeforeNextClick() {
        viewModel.pageIndex.value = 1
        
        assert(!viewModel.panelsNumberHasError)
        viewModel.onNextClick()
        assert(viewModel.panelsNumberHasError)
    }

    @Test
    fun airfoilDataPageStillDisplayedIfDataIsInvalid() = runTest {
        viewModel.pageIndex.value = 1

        viewModel.onDatAirfoilSelected(null)
        viewModel.onPanelsNumberChange(invalidValue)
        viewModel.onNextClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.AIRFOIL_DATA)

        afoilContentResolver.inputStream = datAirfoilFile.inputStream()
        viewModel.onDatAirfoilSelected(TestAfoilContentResolver.testUri)

        advanceUntilIdle()

        viewModel.onNextClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.AIRFOIL_DATA)

        viewModel.onDatAirfoilSelected(null)
        viewModel.onPanelsNumberChange(panelsNumber)
        viewModel.onNextClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.AIRFOIL_DATA)

        afoilContentResolver.inputStream = datAirfoilFile.inputStream()
        viewModel.onDatAirfoilSelected(TestAfoilContentResolver.testUri)

        advanceUntilIdle()

        viewModel.onPanelsNumberChange(panelsNumber)
        viewModel.onNextClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.FLUID_DATA)
    }

    @Test
    fun fluidDataPageEmptyFieldAllowedOnlyBeforeNextClick() {
        viewModel.pageIndex.value = 2
        
        assert(!viewModel.machNumberHasError)
        assert(!viewModel.angleOfAttackHasError)
        viewModel.onNextClick()
        assert(viewModel.machNumberHasError)
        assert(viewModel.angleOfAttackHasError)
    }

    @Test
    fun fluidDataPageEmptyFieldAllowedForReynoldsNumberOnly() {
        viewModel.pageIndex.value = 2

        assert(!viewModel.reynoldsNumberHasError)
        viewModel.onNextClick()
        assert(!viewModel.reynoldsNumberHasError)
    }

    @Test
    fun fluidDataPageStillDisplayedIfDataIsInvalid() {
        viewModel.pageIndex.value = 2

        viewModel.onReynoldsNumberChange(invalidValue)
        viewModel.onMachNumberChange(invalidValue)
        viewModel.onAngleOfAttackChange(invalidValue)
        viewModel.onNextClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.FLUID_DATA)

        viewModel.onReynoldsNumberChange(reynoldsNumber)
        viewModel.onNextClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.FLUID_DATA)

        viewModel.onReynoldsNumberChange(invalidValue)
        viewModel.onMachNumberChange(machNumber)
        viewModel.onNextClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.FLUID_DATA)

        viewModel.onMachNumberChange(invalidValue)
        viewModel.onAngleOfAttackChange(angleOfAttack)
        viewModel.onNextClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.FLUID_DATA)

        viewModel.onReynoldsNumberChange(reynoldsNumber)
        viewModel.onMachNumberChange(machNumber)
        viewModel.onAngleOfAttackChange(invalidValue)
        viewModel.onNextClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.FLUID_DATA)

        viewModel.onReynoldsNumberChange(reynoldsNumber)
        viewModel.onMachNumberChange(invalidValue)
        viewModel.onAngleOfAttackChange(angleOfAttack)
        viewModel.onNextClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.FLUID_DATA)

        viewModel.onReynoldsNumberChange(invalidValue)
        viewModel.onMachNumberChange(machNumber)
        viewModel.onAngleOfAttackChange(angleOfAttack)
        viewModel.onNextClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.FLUID_DATA)

        viewModel.onReynoldsNumberChange(reynoldsNumber)
        viewModel.onMachNumberChange(machNumber)
        viewModel.onAngleOfAttackChange(angleOfAttack)
        viewModel.onNextClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.POST_PROCESSING_SETTINGS)
    }

    @Test
    fun fluidDataPageCanBeLeftWithReynoldsNumberEmpty() {
        viewModel.pageIndex.value = 2

        viewModel.onMachNumberChange(machNumber)
        viewModel.onAngleOfAttackChange(angleOfAttack)
        viewModel.onNextClick()
        assert(viewModel.currentPage == AirfoilAnalysisPage.POST_PROCESSING_SETTINGS)
    }

    @Test
    fun postProcessingSettingsPageEmptyFieldAllowedOnlyBeforeNextClick() {
        viewModel.pageIndex.value = 3
        
        assert(!viewModel.numberOfStreamlinesHasError)
        assert(!viewModel.pressureContoursGridSizeHasError)
        viewModel.onDone()
        assert(viewModel.numberOfStreamlinesHasError)
        assert(viewModel.pressureContoursGridSizeHasError)
    }
    
    @Test
    fun postProcessingSettingsPageStillDisplayedIfDataIsInvalid() {
        viewModel.pageIndex.value = 3

        viewModel.onNumberOfStreamlinesChange(invalidValue)
        viewModel.onPressureContoursGridSizeChange(invalidValue)
        viewModel.onDone()
        assert(viewModel.currentPage == AirfoilAnalysisPage.POST_PROCESSING_SETTINGS)

        viewModel.onNumberOfStreamlinesChange(numberOfStreamlines)
        viewModel.onDone()
        assert(viewModel.currentPage == AirfoilAnalysisPage.POST_PROCESSING_SETTINGS)

        viewModel.onNumberOfStreamlinesChange(invalidValue)
        viewModel.onPressureContoursGridSizeChange(pressureContoursGridSize)
        viewModel.onDone()
        assert(viewModel.currentPage == AirfoilAnalysisPage.POST_PROCESSING_SETTINGS)
    }
}