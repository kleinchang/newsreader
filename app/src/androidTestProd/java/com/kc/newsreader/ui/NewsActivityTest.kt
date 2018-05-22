package com.kc.newsreader.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeDown
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.times
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.Visibility
import android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.kc.newsreader.App
import com.kc.newsreader.R
import com.kc.newsreader.di.*
import com.kc.newsreader.util.TestUtil
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/**
 * Created by changk on 1/20/18.
 */
@RunWith(AndroidJUnit4::class)
class NewsActivityTest {

    private lateinit var server: MockWebServer

    @Rule
    @JvmField var activityTestRule = ActivityTestRule(NewsActivity::class.java, false, false)
    private var mIdlingResource: IdlingResource? = null

    @Before
    fun setUp() {
        Intents.init()

        server = MockWebServer()
        server.start()

        val testAppComponent = prepare(server)
        getApplication().setComponent(testAppComponent)
    }

    @Test
    fun startActivityFail() {
        val jsonResponse = TestUtil.getStringFromFile(getApplication(), "404.json")
        server.enqueue(MockResponse().setResponseCode(404).setBody(jsonResponse).setBodyDelay(2, TimeUnit.SECONDS))

        mIdlingResource = launchActivity(activityTestRule)
        onView(withId(R.id.article_list)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.empty)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun startActivityAndRotate() {
        val jsonResponse = TestUtil.getStringFromFile(getApplication(), "200.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(jsonResponse).setBodyDelay(2, TimeUnit.SECONDS))
        mIdlingResource = launchActivity(activityTestRule)

        onView(withId(R.id.article_list)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.empty)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        rotateOrientation(activityTestRule.activity)

        onView(withId(R.id.article_list)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.empty)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun startActivityThenPullToRefresh() {
        val response200 = TestUtil.getStringFromFile(getApplication(), "200.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(response200).setBodyDelay(2, TimeUnit.SECONDS))
        mIdlingResource = launchActivity(activityTestRule)

        onView(withId(R.id.article_list)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.empty)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        server.enqueue(MockResponse().setResponseCode(200).setBody(response200).setBodyDelay(2, TimeUnit.SECONDS))
        onView(withId(R.id.swipe)).perform(swipeDown())

        onView(withId(R.id.article_list)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.empty)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun startActivityThenPullToRefreshFail() {
        val response200 = TestUtil.getStringFromFile(getApplication(), "200.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(response200).setBodyDelay(2, TimeUnit.SECONDS))

        mIdlingResource = launchActivity(activityTestRule)
        onView(withId(R.id.article_list)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.empty)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        val response503 = TestUtil.getStringFromFile(getApplication(), "503.json")
        server.enqueue(MockResponse().setResponseCode(503).setBody(response503).setBodyDelay(2, TimeUnit.SECONDS))

        onView(withId(R.id.swipe)).perform(swipeDown())
        onView(withId(R.id.article_list)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.empty)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun clickArticleAndOpenUrl() {
        val response200 = TestUtil.getStringFromFile(getApplication(), "200.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(response200).setBodyDelay(2, TimeUnit.SECONDS))

        mIdlingResource = launchActivity(activityTestRule)
        onView(withId(R.id.article_list)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        val size = activityTestRule.activity.getAdapter().list.size - 1
        for (position in 0 .. size) {
            onView(withId(R.id.article_list)).perform(scrollToPosition<ArticleAdapter.ViewHolder>(position))
            onView(withId(R.id.article_list)).perform(actionOnItemAtPosition<ArticleAdapter.ViewHolder>(position, click()))
            Intents.intended(IntentMatchers.hasComponent(WebViewActivity::class.java.name), times(position + 1))
            onView(withId(R.id.webview)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
            Espresso.pressBack()
        }
    }

    @After
    fun teardown() {
        Intents.release()
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
            mIdlingResource = null
        }
    }

    private fun launchActivity(rule: ActivityTestRule<NewsActivity>): IdlingResource? {
        rule.launchActivity(Intent())
        var idlingResource = rule.activity.getCountingIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
        return idlingResource
    }

    private fun rotateOrientation(activity: Activity) {
        val currentOrientation = activity.resources.configuration.orientation

        when (currentOrientation) {
            Configuration.ORIENTATION_LANDSCAPE -> rotateToPortrait(activity)
            Configuration.ORIENTATION_PORTRAIT -> rotateToLandscape(activity)
            else -> rotateToLandscape(activity)
        }
    }

    private fun rotateToLandscape(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun rotateToPortrait(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun getApplication(): App {
        return InstrumentationRegistry.getTargetContext().applicationContext as App
    }

    private fun prepare(server: MockWebServer): TestAppComponent {
        return DaggerTestAppComponent.builder()
                .appModule(AppModule(getApplication()))
                .dataModule(object : DataModule() {
                    override fun provideAppConfig(): AppConfig {
                        return AppConfig(server.url("/").toString())
                    }
                })
                .build()
    }
}