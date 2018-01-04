package com.vipheyue.fadetop

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.vipheyue.fadetop", appContext.packageName)
    }

    @Test
    fun dataTest() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val calendar = Calendar.getInstance()
        calendar . set (2018, 2, 15, 8, 31, 5)
        var timeInMillis = calendar.timeInMillis
        var timeInMilli2 = calendar.timeInMillis
        assertEquals("com.vipheyue.fadetop", calendar.timeInMillis.toString())
    }
}
