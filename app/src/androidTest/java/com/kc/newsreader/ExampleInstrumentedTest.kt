package com.kc.newsreader

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.kc.newsreader.data.model.Edition
import com.kc.newsreader.util.TestUtil
import com.google.gson.Gson
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

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
        assertEquals("com.kc.newsreader", appContext.packageName)

        val json = TestUtil.getStringFromFile(appContext, "full.json")
        val data = Gson().fromJson<Edition.CollectionData>(json, Edition.CollectionData::class.java)
        Assert.assertEquals(13, data.articleList.size)
        data.articleList.sortedByDescending { it.timestamp }
        Assert.assertEquals(13, data.articleList.size)
    }
}
