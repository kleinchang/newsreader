package com.kc.newsreader.data.source

import android.content.Context
import android.os.Handler
import com.kc.newsreader.data.model.Edition
import com.kc.newsreader.util.TestUtil
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Created by changk on 1/19/18.
 */
class MockDataSource @Inject constructor(val context: Context) : DataSource {

    override fun getArticles(callback: DataSource.LoadCallback) {

        val json = TestUtil.getStringFromFile(context, "200.json")
        val data = Gson().fromJson<Edition.CollectionData>(json, Edition.CollectionData::class.java)
        if (data.articleList != null) {
            Handler().postDelayed({
                callback.onLoaded(data.articleList.sortedByDescending { it.timestamp })
            }, 500)
        } else {
            callback.onUnavailable()
        }
    }

    override fun disposeCache() {
        // No need for this as this is a fake data source
    }
}