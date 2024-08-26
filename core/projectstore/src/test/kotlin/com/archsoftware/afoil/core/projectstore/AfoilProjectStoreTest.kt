package com.archsoftware.afoil.core.projectstore

import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.AirfoilAnalysisProjectData
import com.archsoftware.afoil.core.testing.contentresolver.TestAfoilContentResolver
import com.archsoftware.afoil.core.testing.repository.TestUserPreferencesRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AfoilProjectStoreTest {

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testScheduler = TestCoroutineScheduler()
    private val afoilContentResolver = TestAfoilContentResolver()
    private val userPreferencesRepository = TestUserPreferencesRepository()
    private val projectStore = AfoilProjectStore(
        contentResolver = afoilContentResolver,
        preferencesRepository = userPreferencesRepository,
        ioDispatcher = StandardTestDispatcher(testScheduler)
    )

    // Test data
    private val project = AfoilProject(
        name = "Project 1",
        projectDataType = AirfoilAnalysisProjectData::class.java.name
    )
    private val datAirfoilDisplayName = "NACA0012.dat"

    @Test
    fun readWriteProjectData() = runTest(StandardTestDispatcher(testScheduler)) {
        val projectData = AirfoilAnalysisProjectData(
            datAirfoilDisplayName = datAirfoilDisplayName,
            panelsNumber = 100,
            reynoldsNumber = 20e3,
            machNumber = 0.3,
            angleOfAttack = 5.0,
            numberOfStreamlines = 50,
            streamlinesRefiningLevel = 0.5f,
            pressureContoursGridSize = 50,
            pressureContoursRefiningLevel = 0.5f
        )
        val projectDataFile = tmpFolder.newFile(PROJECT_DATA_FILE_NAME)
        afoilContentResolver.inputStream = projectDataFile.inputStream()
        afoilContentResolver.outputStream = projectDataFile.outputStream()

        userPreferencesRepository.updateAfoilProjectsDirectory(TestAfoilContentResolver.testUri.toString())
        projectStore.setProjectDir(project)
        projectStore.writeProjectData(projectData)

        val readProjectData = projectStore.readProjectData()

        assert(readProjectData == projectData)
    }
}