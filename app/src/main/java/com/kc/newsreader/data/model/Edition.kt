package com.kc.newsreader.data.model

import com.kc.newsreader.util.TestOpen
import com.google.gson.annotations.SerializedName

/**
 * Created by changk on 1/19/18.
 */
class Edition {

    data class CollectionData(@SerializedName("assets") val articleList: List<Article>)

    @TestOpen
    data class Article(@SerializedName("headline") val headline: String,
                       @SerializedName("theAbstract") val abstract: String,
                       @SerializedName("byLine") val byline: String,
                       @SerializedName("url") val url: String,
                       @SerializedName("timeStamp") val timestamp: String,
                       @SerializedName("relatedImages") val imageList: List<Image>,
                       var selectedImage: Image)

    data class Image(@SerializedName("url") val url: String? = null,
                     @SerializedName("width") val width: Int = 0,
                     @SerializedName("height") val height: Int = 0)
}