package com.kc.newsreader.di

import com.kc.newsreader.ui.NewsActivityTest
import dagger.Component
import javax.inject.Singleton

/**
 * Created by changk on 1/20/18.
 */

@Singleton
@Component(modules = [AppModule::class, DataModule::class, RepositoryModule::class])
interface TestAppComponent : AppComponent {

    fun inject(activity: NewsActivityTest)
}