package com.archsoftware.afoil.core.common

import com.archsoftware.afoil.core.common.utils.DatAirfoilReader
import com.archsoftware.afoil.core.testing.contentresolver.TestAfoilContentResolver
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DatAirfoilReaderTest {

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testScheduler = TestCoroutineScheduler()
    private val afoilContentResolver = TestAfoilContentResolver()
    private val datAirfoilReader = DatAirfoilReader(
        contentResolver = afoilContentResolver,
        ioDispatcher = StandardTestDispatcher(testScheduler)
    )

    // Test data
    private val airfoilName = "NACA 0012"

    @Test
    fun readName() = runTest(StandardTestDispatcher(testScheduler)) {
        val datAirfoilFile = tmpFolder.newFile("naca0012.dat")
        datAirfoilFile.writeText(airfoilName)
        afoilContentResolver.inputStream = datAirfoilFile.inputStream()

        val name = datAirfoilReader.readName(TestAfoilContentResolver.testUri)

        assert(name == airfoilName)
    }
}