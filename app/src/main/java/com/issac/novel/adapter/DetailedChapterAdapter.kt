package com.issac.novel.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.issac.novel.R
import com.issac.novel.extract.DetailedChapterItem


class DetailedChapterAdapter(private val data: List<DetailedChapterItem>): RecyclerView.Adapter<DetailedChapterAdapter.UpdateViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chapter_detailed, parent, false)
        return UpdateViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: UpdateViewHolder, position: Int) {
        holder.titleTv?.text = data[position].name
        holder.chapterTv?.text = data[position].chapter
        holder.authorTv?.text = data[position].author
    }

    inner class UpdateViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var titleTv: TextView? = null
        var chapterTv: TextView? = null
        var authorTv: TextView? = null

        init {
            titleTv = view.findViewById<TextView>(R.id.title_tv)
            chapterTv = view.findViewById<TextView>(R.id.chapter_tv)
            authorTv = view.findViewById<TextView>(R.id.author_tv)
        }
    }
}

class ChapterItemDecoration(context: Context): RecyclerView.ItemDecoration() {
    private var dividerHeight: Float
    private var dividerPaint: Paint = Paint()

    init {

        val dip = 1f

        dividerHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            context.resources.displayMetrics
        )
        dividerPaint.color = context.getColor(R.color.divider)
    }

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.bottom = dividerHeight.toInt()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        val left = parent.paddingLeft.toFloat()
        val right = parent.width - parent.paddingRight.toFloat()

        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            val top = view.bottom.toFloat()
            val bottom = (view.bottom + dividerHeight)
            c.drawRect(left, top, right, bottom, dividerPaint)
        }

    }
}
