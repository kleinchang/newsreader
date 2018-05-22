package com.kc.newsreader.ui

import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.Snackbar
import android.support.test.espresso.IdlingResource
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.kc.newsreader.App
import com.kc.newsreader.R
import com.kc.newsreader.data.NewsRepository
import com.kc.newsreader.data.model.Edition
import com.kc.newsreader.util.EspressoIdlingResource

import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.content_news.*
import javax.inject.Inject

class NewsActivity : AppCompatActivity(), NewsContract.View {

    @Inject
    lateinit var repository: NewsRepository

    override lateinit var presenter: NewsContract.Presenter

    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_news)
        setSupportActionBar(toolbar)

        App.appComponent.inject(this)
        presenter = NewsPresenter(repository, this)

        article_list.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        }

        swipe.setOnRefreshListener { presenter.loadArticles(true) }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadArticles(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_news, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun populateData(articleList: List<Edition.Article>) {
        articleAdapter = ArticleAdapter(articleList, itemClickListener)
    }

    override fun loadingIndicator(active: Boolean) {
        swipe.isRefreshing = active
    }

    override fun hideArticles() {
        article_list.visibility = View.GONE
        empty.visibility = View.VISIBLE
    }

    override fun showArticles(articles: List<Edition.Article>) {
        System.out.println("Kai: NewsActivity showArticles: ${Thread.currentThread().name}")

        article_list.adapter.notifyDataSetChanged()
        article_list.visibility = View.VISIBLE
        empty.visibility = View.GONE
    }

    override fun openArticle(url: String) {
        WebViewActivity.open(this, url)
    }

    private val itemClickListener = object : OnItemClickListener {
        override fun onClick(position: Int) {
            System.out.println("Kai: NewsActivity onClick $position")
            presenter.clickOnArticle(position)
        }
    }

    @VisibleForTesting
    fun getCountingIdlingResource(): IdlingResource {
        return EspressoIdlingResource.countingIdlingResource
    }

    @VisibleForTesting
    fun getAdapter(): ArticleAdapter {
        return articleAdapter
    }
}
