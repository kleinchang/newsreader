package com.kc.newsreader

import android.app.Application
import android.support.annotation.VisibleForTesting
import com.facebook.drawee.backends.pipeline.Fresco
import com.kc.newsreader.di.AppComponent
import com.kc.newsreader.di.AppModule
import com.kc.newsreader.di.DaggerAppComponent

/**
 * Created by changk on 1/20/18.
 */
class App : Application() {

    companion object {
        @JvmStatic lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this)).build()
    }

    @VisibleForTesting
    fun setComponent(component: AppComponent) {
        appComponent = component
    }
}