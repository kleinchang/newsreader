package com.kc.newsreader.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kc.newsreader.R
import com.kc.newsreader.data.model.Edition
import kotlinx.android.synthetic.main.row_article.view.*

/**
 * Created by changk on 1/19/18.
 */
class ArticleAdapter(var list: List<Edition.Article>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_article, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        (holder as ViewHolder).itemView.apply {
            headline.text = list[position].headline
            the_abstract.text = list[position].abstract
            byline.text = list[position].byline
            val selected = list[position].selectedImage
            image.aspectRatio = 1.0f * selected.width / selected.height
            image.setImageURI(selected.url)
            System.out.println("Position: $position ratio: ${image.aspectRatio}")
            timestamp.text = list[position].timestamp
        }
//        val image = list[position].selectedImage
//        Picasso.with(holder.itemView.image.context).load(image.url).resize(image.width, image.height).into(holder.itemView.image)

        holder.itemView.setOnClickListener { itemClickListener.onClick(position) }

//        System.out.println("Kai: load $position ratio:${holder.itemView.image.aspectRatio} ${list[position].selectedImage.url}")
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}