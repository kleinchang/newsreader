package com.kc.newsreader.data.source

import com.kc.newsreader.data.model.Edition
/**
 * Created by changk on 1/19/18.
 */
interface DataSource {

    interface LoadCallback {

        fun onLoaded(articles: List<Edition.Article>)

        fun onUnavailable()
    }

    fun getArticles(callback: LoadCallback)

    fun disposeCache()
}