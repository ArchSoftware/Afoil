package com.archsoftware.afoil.core.common

import com.archsoftware.afoil.core.common.utils.format
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NumberUtilsTest {

    @Test
    fun format() {
        val number = 123456.789
        val formatted = number.format(2)
        assert(formatted == "123456.79")
    }
}