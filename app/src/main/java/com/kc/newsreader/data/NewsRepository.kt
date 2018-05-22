package com.kc.newsreader.data

import com.kc.newsreader.data.model.Edition
import com.kc.newsreader.data.source.DataSource
import com.kc.newsreader.util.TestOpen
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by changk on 1/19/18.
 */
@TestOpen
@Singleton
class NewsRepository @Inject constructor(private val remote: DataSource/*, private val local: DataSource*/) : DataSource {

    var cachedEvents = ArrayList<Edition.Article>()

    var cacheIsDirty = false

    override fun getArticles(callback: DataSource.LoadCallback) {

        if (cachedEvents.isNotEmpty() && !cacheIsDirty) {
            System.out.println("Kai: NewsRepository getArticles cache ${Thread.currentThread().name}")
            callback.onLoaded(cachedEvents)
            return
        } else {
            System.out.println("Kai: NewsRepository getArticles not cache ${Thread.currentThread().name}")
        }

        remote.getArticles(object : DataSource.LoadCallback {

            override fun onLoaded(articles: List<Edition.Article>) {
                System.out.println("Kai: NewsRepository onLoaded ${Thread.currentThread().name}")
                updateCache(articles)
                callback.onLoaded(articles)
            }

            override fun onUnavailable() {
                System.out.println("Kai: NewsRepository onUnavailable ${Thread.currentThread().name}")
                callback.onUnavailable()
            }
        })
    }

    override fun disposeCache() {
        cacheIsDirty = true
    }

    fun updateCache(articles: List<Edition.Article>) {
        cachedEvents.clear()
        cachedEvents.addAll(articles)
    }
}