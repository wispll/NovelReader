package com.issac.novel.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.issac.novel.R
import com.issac.novel.adapter.ChapterItemDecoration
import com.issac.novel.adapter.DetailedChapterAdapter
import com.issac.novel.db.NovelHistory
import com.issac.novel.extract.LatestChapter
import com.issac.novel.model.HistoryModel
import com.issac.novel.util.Util
import kotlinx.android.synthetic.main.fragment_bookshelf.*

class BookshelfFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookshelf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        history_recycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        history_recycleView.addItemDecoration(ChapterItemDecoration(requireContext()))

        val model= ViewModelProvider(requireActivity()).get(HistoryModel::class.java)
        model.queryAll().observe(viewLifecycleOwner,
            Observer<Array<NovelHistory>> { novelHistories->
                if(novelHistories.isNotEmpty()){

                    val map = novelHistories.map {
                        LatestChapter(
                            "",
                            it.title,
                            it.chapter,
                            it.author,
                            "",
                            it.href
                        )
                    }
                    val ad = DetailedChapterAdapter(map)
                    ad.setItemClickListener {
                        Util.goReader(requireActivity(), Arguments(
                            it.content_href,
                            it.article_title,
                            it.author
                            )
                        )
                    }
                    history_recycleView.adapter = ad

                }
            }
        )
    }
}