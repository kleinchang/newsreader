package com.kc.newsreader.ui

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kc.newsreader.R
import com.kc.newsreader.util.Const
import kotlinx.android.synthetic.main.activity_webview.*


/**
 * Created by changk on 1/19/18.
 */
class WebViewActivity : AppCompatActivity() {

    companion object {
        fun open(activity: Activity, url: String) {
            val repositoryDetailsIntent = Intent(activity, WebViewActivity::class.java)
            repositoryDetailsIntent.putExtra(Const.KEY_URL, url)
            activity.startActivity(repositoryDetailsIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: no loading indicator at the moment
        setContentView(R.layout.activity_webview)

        val url = intent.extras.getString(Const.KEY_URL)
        webview.webViewClient = object : WebViewClient() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        webview.loadUrl(url)
    }
}