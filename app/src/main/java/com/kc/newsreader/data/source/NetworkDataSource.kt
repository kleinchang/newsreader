package com.kc.newsreader.data.source

import android.content.Context
import android.os.Handler
import com.kc.newsreader.data.model.Edition
import com.kc.newsreader.di.AppConfig
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import okhttp3.*
import java.io.IOException
import java.io.StringReader
import javax.inject.Inject

/**
 * Created by changk on 1/19/18.
 */
class NetworkDataSource @Inject constructor(val context: Context) : DataSource {

    @Inject lateinit var gson: Gson
    @Inject lateinit var client: OkHttpClient
    @Inject lateinit var mainHandler: Handler
    @Inject lateinit var config: AppConfig

    override fun getArticles(callback: DataSource.LoadCallback) {

        val request = Request.Builder().url(config.endpoint).build()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call?, response: Response?) {
                System.out.println("Kai: NetworkDataSource onResponse ${Thread.currentThread().name}")
                val reader = JsonReader(StringReader(response?.body()?.string()))
                val data = gson.fromJson<Edition.CollectionData>(reader, Edition.CollectionData::class.java)
                if (data.articleList != null) {
                    mainHandler.postDelayed({
                        callback.onLoaded(data.articleList.sortedByDescending { it.timestamp })
                    }, 500)
                } else {
                    System.out.println("Kai: NetworkDataSource onResponse data.articleList == null")
                    mainHandler.postDelayed({
                        callback.onUnavailable()
                    }, 500)
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                System.out.println("Kai: NetworkDataSource getArticles onFailure")
                mainHandler.postDelayed({
                    callback.onUnavailable()
                }, 500)
            }
        })


    }

    override fun disposeCache() {
        // No need for this as this is a fake data source
    }
}