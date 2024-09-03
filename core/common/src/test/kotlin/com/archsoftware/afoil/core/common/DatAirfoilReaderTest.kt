package com.archsoftware.afoil.core.common

import com.archsoftware.afoil.core.common.datairfoilreader.Coordinate
import com.archsoftware.afoil.core.common.datairfoilreader.DatAirfoilReader
import com.archsoftware.afoil.core.testing.contentresolver.TestAfoilContentResolver
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
    private val validAirfoil =
        " NACA 0012 AIRFOILS\n" +
                "1.0000000 0.0012600\n" +
                "0.9994161 0.0013419\n" +
                "0.9994161 -.0013419\n" +
                "1.0000000 -.0012600\n"
    private val invalidAirfoilFirstLineNotTwoParts =
        " NACA 0012 AIRFOILS\n" +
                "1.0000000 0.0012600   0.00099\n" +
                "0.9994161 0.0013419\n" +
                "0.9994161 -.0013419\n" +
                "1.0000000 -.0012600\n"
    private val invalidAirfoilLastLineNotTwoParts =
        " NACA 0012 AIRFOILS\n" +
                "1.0000000 0.0012600\n" +
                "0.9994161 0.0013419\n" +
                "0.9994161 -.0013419\n" +
                "1.0000000 -.0012600   0.00099\n"
    private val invalidAirfoilStartXNot1 =
        " NACA 0012 AIRFOILS\n" +
                "1.0009089 0.0012600\n" +
                "0.9994161 0.0013419\n" +
                "0.9994161 -.0013419\n" +
                "1.0000000 -.0012600\n"
    private val invalidAirfoilEndXNot1 =
        " NACA 0012 AIRFOILS\n" +
                "1.0000000 0.0012600\n" +
                "0.9994161 0.0013419\n" +
                "0.9994161 -.0013419\n" +
                "1.0009089 -.0012600\n"
    private val airfoilName = " NACA 0012 AIRFOILS"
    private val airfoilCoordinates = listOf(
        Coordinate(1.0000000, 0.0012600),
        Coordinate(0.9994161, 0.0013419),
        Coordinate(0.9994161, -.0013419),
        Coordinate(1.0000000, -.0012600)
    )

    private lateinit var datAirfoilFile: File

    @Before
    fun setup() {
        datAirfoilFile = tmpFolder.newFile("NACA0012.dat")
        afoilContentResolver.inputStream = datAirfoilFile.inputStream()
    }

    @Test
    fun checkValidity_invalidAirfoilFirstLineNotTwoParts() = runTest(StandardTestDispatcher(testScheduler)) {
        datAirfoilFile.writeText(invalidAirfoilFirstLineNotTwoParts)
        assertFalse(datAirfoilReader.checkValidity(TestAfoilContentResolver.testUri))
    }

    @Test
    fun checkValidity_invalidAirfoilLastLineNotTwoParts() = runTest(StandardTestDispatcher(testScheduler)) {
        datAirfoilFile.writeText(invalidAirfoilLastLineNotTwoParts)
        assertFalse(datAirfoilReader.checkValidity(TestAfoilContentResolver.testUri))
    }

    @Test
    fun checkValidity_invalidAirfoilStartXNot1() = runTest(StandardTestDispatcher(testScheduler)) {
        datAirfoilFile.writeText(invalidAirfoilStartXNot1)
        assertFalse(datAirfoilReader.checkValidity(TestAfoilContentResolver.testUri))
    }

    @Test
    fun checkValidity_invalidAirfoilEndXNot1() = runTest(StandardTestDispatcher(testScheduler)) {
        datAirfoilFile.writeText(invalidAirfoilEndXNot1)
        assertFalse(datAirfoilReader.checkValidity(TestAfoilContentResolver.testUri))
    }

    @Test
    fun checkValidity_validAirfoil() = runTest(StandardTestDispatcher(testScheduler)) {
        datAirfoilFile.writeText(validAirfoil)
        assertTrue(datAirfoilReader.checkValidity(TestAfoilContentResolver.testUri))
    }

    @Test
    fun readName() = runTest(StandardTestDispatcher(testScheduler)) {
        datAirfoilFile.writeText(validAirfoil)
        val name = datAirfoilReader.readName(TestAfoilContentResolver.testUri)

        assertEquals(airfoilName, name)
    }

    @Test
    fun readCoordinates() = runTest(StandardTestDispatcher(testScheduler)) {
        datAirfoilFile.writeText(validAirfoil)
        val coordinates = datAirfoilReader.readCoordinates(TestAfoilContentResolver.testUri)

        assertContentEquals(airfoilCoordinates, coordinates)
    }
}