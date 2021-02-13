package com.issac.novel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.issac.novel.R
import com.issac.novel.extract.Chapter


class SimpleChapterAdapter(private val data: List<Chapter>): RecyclerView.Adapter<SimpleChapterAdapter.DetailViewHolder>() {

    lateinit var mItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chapter_simple, parent, false)
        return DetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.chapterTv?.text = data[position].name
        holder.chapterTv?.setOnClickListener{
            mItemClickCallback(data[position])
        }
    }


    inner class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var chapterTv: TextView? = null

        init {
            chapterTv = view.findViewById(R.id.chapter_tv)
        }
    }


    fun setItemClickListener(callback: OnItemClickCallback){
            mItemClickCallback = callback
    }
}

typealias OnItemClickCallback = (Chapter) -> Unit

