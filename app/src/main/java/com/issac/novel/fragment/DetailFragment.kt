package com.issac.novel.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.issac.novel.R
import com.issac.novel.adapter.ChapterItemDecoration
import com.issac.novel.adapter.SimpleChapterAdapter
import com.issac.novel.db.NovelHistory
import com.issac.novel.extract.Detail
import com.issac.novel.extract.SimpleChapter
import com.issac.novel.model.DetailModel
import com.issac.novel.model.HistoryModel
import kotlinx.android.synthetic.main.fragment_detail.*
import timber.log.Timber
import java.util.*

class DetailFragment: Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chapter_recycle_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        chapter_recycle_view.addItemDecoration(ChapterItemDecoration(requireContext()))

        appbar_layout_toolbar.setNavigationOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

        try {

            val model= ViewModelProvider(requireActivity()).get(DetailModel::class.java)
            val href = arguments?.getString("href")
            val liveData = model.getLiveData(href!!)

            liveData?.observe(viewLifecycleOwner,
                Observer<Detail> { data ->

                    spin_kit.visibility = View.GONE

                    Glide.with(poster_iv)
                        .load(data.image)
                        .into(poster_iv)
                    title_tv.text = data.title
                    author_tv.text = data.author
                    intro_tv.text = data.intro
                    latest_chapter_tv.text = data.latest_chapter

                    val adapter = SimpleChapterAdapter(data.chapter_list)
                    chapter_recycle_view.adapter = adapter
                    adapter.setItemClickListener{
                            goReader(it.href)
                            saveReadHistory(data, it)
                    }
                }
            )
        }catch (e: Exception){

            Timber.e(e)
            Toast.makeText(this.requireContext(), e.message, Toast.LENGTH_SHORT).show()

        }
    }

    private fun goReader(href: String){
        val fragment = ReaderFragment()
        val bundle = Bundle()
        bundle.putString("href", href)
        fragment.arguments  = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment, "readerFragment")
            .addToBackStack("reader")
            .commit()
    }

    private fun saveReadHistory(data: Detail, chapter: SimpleChapter){

        val model= ViewModelProvider(requireActivity()).get(HistoryModel::class.java)
        val item = NovelHistory(
            UUID.nameUUIDFromBytes(data.title.toByteArray()).toString() ,data.title,data.author,chapter.name,chapter.href,System.currentTimeMillis()
        )
        model.save2Db(item)
    }

}