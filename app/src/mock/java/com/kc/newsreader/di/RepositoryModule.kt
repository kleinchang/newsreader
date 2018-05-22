package com.kc.newsreader.di

import com.kc.newsreader.data.source.DataSource
import com.kc.newsreader.data.source.MockDataSource
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by changk on 1/20/18.
 */

@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideDataSource(mock: MockDataSource): DataSource
}