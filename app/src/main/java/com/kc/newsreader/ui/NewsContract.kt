package com.kc.newsreader.ui

import com.kc.newsreader.data.model.Edition
import com.kc.newsreader.ui.base.BaseView

/**
 * Created by changk on 1/19/18.
 */
interface NewsContract {

    interface View : BaseView<Presenter> {

        fun populateData(articleList: List<Edition.Article>)

        fun loadingIndicator(active: Boolean)

        fun hideArticles()

        fun showArticles(events: List<Edition.Article>)

        fun openArticle(url: String)

    }

    interface Presenter {

        fun loadArticles(forceUpdate: Boolean)

        fun clickOnArticle(position: Int)
    }
}