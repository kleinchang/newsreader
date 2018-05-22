package com.kc.newsreader

import com.kc.newsreader.data.NewsRepository
import com.kc.newsreader.data.model.Edition
import com.kc.newsreader.data.source.DataSource
import com.kc.newsreader.ui.NewsContract
import com.kc.newsreader.ui.NewsPresenter
import com.kc.newsreader.util.capture
import com.google.common.collect.Lists
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*

/**
 * Created by changk on 1/20/18.
 */
class NewsPresenterTest {

    @Mock private lateinit var mView: NewsContract.View

    @Mock private lateinit var mRepository: NewsRepository

    @Captor private lateinit var loadCallbackCaptor: ArgumentCaptor<DataSource.LoadCallback>
    @Captor private lateinit var viewCallbackCaptor: ArgumentCaptor<List<Edition.Article>>


    private lateinit var mPresenter: NewsPresenter

    private val listURL = arrayOf("http://url_1", "http://url_2", "http://url_3")

    private lateinit var articles: MutableList<Edition.Article>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mPresenter = NewsPresenter(mRepository, mView)
        articles = Lists.newArrayList(mockArticle(listURL[0]), mockArticle(listURL[1]), mockArticle(listURL[2]))
    }

    private fun mockArticle(articleUrl: String): Edition.Article {
        return mock(Edition.Article::class.java).apply {
            `when`(url).thenReturn(articleUrl)
        }
    }

    @Test
    fun createPresenterThenSetToView() {
        mPresenter = NewsPresenter(mRepository, mView)
        verify(mView).presenter = mPresenter
    }

    @Test
    fun loadArticlesFromRepositoryIntoView() {
        mPresenter.loadArticles(false)

        verify(mRepository).getArticles(capture(loadCallbackCaptor))
        loadCallbackCaptor.value.onLoaded(articles)

        val inOrder = Mockito.inOrder(mView)
        inOrder.verify(mView).loadingIndicator(true)
        inOrder.verify(mView).loadingIndicator(false)

        verify(mView).showArticles(capture(viewCallbackCaptor))
        Assert.assertTrue(viewCallbackCaptor.value.size == articles.size)
    }

    @Test
    fun loadArticlesNotAvailable() {
        mPresenter.loadArticles(true)

        verify(mRepository).getArticles(capture(loadCallbackCaptor))
        loadCallbackCaptor.value.onUnavailable()

        val inOrder = Mockito.inOrder(mView)
        inOrder.verify(mView).loadingIndicator(true)
        inOrder.verify(mView).loadingIndicator(false)


        verify(mView).hideArticles()
    }

    @Test
    fun clickOnArticle() {
        mPresenter.setArticles(articles)

        articles.forEachIndexed { index, _ ->
            mPresenter.clickOnArticle(index)
            verify(mView).openArticle(listURL[index])
        }
    }
}