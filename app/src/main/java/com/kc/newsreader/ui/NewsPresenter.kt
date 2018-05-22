package com.kc.newsreader.ui

import android.support.annotation.VisibleForTesting
import com.kc.newsreader.data.NewsRepository
import com.kc.newsreader.data.model.Edition
import com.kc.newsreader.data.source.DataSource
import com.kc.newsreader.util.EspressoIdlingResource
import java.util.ArrayList

/**
 * Created by changk on 1/19/18.
 */


class NewsPresenter(val repository: NewsRepository, val view: NewsContract.View) : NewsContract.Presenter {

    var articleList = ArrayList<Edition.Article>()

    init {
        view.presenter = this
        view.populateData(articleList)
    }

    override fun loadArticles(forceUpdate: Boolean) {
        view.loadingIndicator(true)

        if (forceUpdate)
            repository.disposeCache()

        setIsAppBusy(true)
        repository.getArticles(object : DataSource.LoadCallback {

            override fun onLoaded(articles: List<Edition.Article>) {
                System.out.println("Kai: NewsPresenter getArticles: onLoaded ${Thread.currentThread().name}")
                view.loadingIndicator(false)

                /***
                 * We can prepare space for image view in advance according to the size of images.
                 * This can achieve better (than using wrap_content) scrolling experience.
                 * So images without proper dimensions specified are discarded here.
                  */
                articles.forEach {
                    it.selectedImage = it.imageList
                                    .filter { it.width != 0 && it.height != 0 }
                                    .minBy { it.width + it.height } ?: Edition.Image()
                }
                articleList.clear()
                articleList.addAll(articles)
                view.showArticles(articles)

                setIsAppBusy(false)
                System.out.println("Kai: NewsPresenter getArticles onLoaded decrement")
            }

            override fun onUnavailable() {
                System.out.println("Kai: NewsPresenter getArticles: onUnavailable ${Thread.currentThread().name}")
                view.loadingIndicator(false)
                view.hideArticles()

                setIsAppBusy(false)
                System.out.println("Kai: NewsPresenter getArticles onUnavailable decrement")
            }
        })
    }

    override fun clickOnArticle(position: Int) {
        view.openArticle(articleList[position].url)
    }

    /**
     * To signal if App is still busy for Espresso test's reference
     */
    private fun setIsAppBusy(busy: Boolean) {
        if (busy) {
            EspressoIdlingResource.increment() // App is busy until further notice
        } else {
            if (!EspressoIdlingResource.countingIdlingResource.isIdleNow)
                EspressoIdlingResource.decrement() // Set app as idle.
        }
    }

    @VisibleForTesting
    fun setArticles(articles: List<Edition.Article>) {
        articleList.clear()
        articleList.addAll(articles)
    }
}