package com.kc.newsreader.di

import android.content.Context
import android.os.Handler
import com.kc.newsreader.util.Const
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Created by changk on 1/20/18.
 */

@Module
open class DataModule {

    @Provides @Singleton
    open fun provideAppConfig(): AppConfig {
        return AppConfig(Const.ENDPOINT)
    }

    @Provides @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    @Provides @Singleton
    fun provideMainHandler(context: Context): Handler {
        return Handler(context.mainLooper)
    }
}