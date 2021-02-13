package com.issac.novel.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
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
import com.issac.novel.extract.Detail
import com.issac.novel.model.DetailModel
import com.issac.novel.util.Util
import kotlinx.android.synthetic.main.fragment_detail.*
import timber.log.Timber

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

                    latest_chapter_tv.text =  getHyperlinkSpannableString(data.latest_chapter)
                    latest_chapter_tv.setOnClickListener{
                        Util.goReader(requireActivity(), Arguments(
                            data.latest_chapter_href,
                            data.title,
                            data.author
                            )
                        )
                    }

                    val adapter = SimpleChapterAdapter(data.chapter_list)
                    chapter_recycle_view.adapter = adapter
                    adapter.setItemClickListener{
                            Util.goReader(requireActivity(), Arguments(
                                it.href,
                                data.title,
                                data.author
                                )
                            )
                    }
                }
            )
        }catch (e: Exception){

            Timber.e(e)
            Toast.makeText(this.requireContext(), e.message, Toast.LENGTH_SHORT).show()

        }
    }

    private fun getHyperlinkSpannableString(str: String): SpannableString{

        val spannableString = SpannableString(str)
        spannableString.setSpan(UnderlineSpan(), 0, str.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#338de6")), 0, str.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannableString
    }

}