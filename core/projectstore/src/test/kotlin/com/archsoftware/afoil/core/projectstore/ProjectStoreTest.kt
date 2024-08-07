package com.archsoftware.afoil.core.projectstore

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
class ProjectStoreTest {

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testScheduler = TestCoroutineScheduler()
    private val afoilContentResolver = TestAfoilContentResolver()
    private val userPreferencesRepository = TestUserPreferencesRepository()
    private val projectStore = ProjectStore(
        contentResolver = afoilContentResolver,
        preferencesRepository = userPreferencesRepository,
        ioDispatcher = StandardTestDispatcher(testScheduler)
    )

    @Test
    fun readWriteProjectData() = runTest(StandardTestDispatcher(testScheduler)) {
        val projectData = AirfoilAnalysisProjectData(
            datAirfoilUri = afoilContentResolver.testUri.toString(),
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

        projectStore.writeProjectData(
            uri = afoilContentResolver.testUri,
            projectData = projectData,
            type = AirfoilAnalysisProjectData::class.java
        )

        val readProjectData = projectStore.readProjectData(
            uri = afoilContentResolver.testUri,
            type = AirfoilAnalysisProjectData::class.java
        )

        assert(readProjectData == projectData)
    }
}