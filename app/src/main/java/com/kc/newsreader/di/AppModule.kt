package com.kc.newsreader.di

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by changk on 1/20/18.
 */

@Module
class AppModule constructor(val context: Context) {

    @Provides
    fun provideContext(): Context = context
}