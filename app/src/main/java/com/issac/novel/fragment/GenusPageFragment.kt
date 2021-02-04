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
import com.issac.novel.R
import com.issac.novel.adapter.PopularBannerAdapter
import com.issac.novel.adapter.DetailedChapterAdapter
import com.issac.novel.adapter.ChapterItemDecoration
import com.issac.novel.extract.Novel
import com.issac.novel.model.NovelModel
import kotlinx.android.synthetic.main.fragment_genus_page.*
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.AlphaPageTransformer
import timber.log.Timber
import java.lang.Exception


class GenusPageFragment: Fragment() {

    companion object{
        const val HREF_ARG = "href_arg"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_genus_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {

            val model: NovelModel= ViewModelProvider(requireActivity()).get(NovelModel::class.java)
            val href = arguments?.getString(HREF_ARG)
            val liveData = model.getLiveData(href!!)

            liveData?.observe(viewLifecycleOwner,
                Observer<Novel> { data ->

                    val adapter = PopularBannerAdapter(data.hotItemList)
                    banner.adapter = adapter
                    banner.addBannerLifecycleObserver(this)
                    banner.addPageTransformer(AlphaPageTransformer())
                    banner.setBannerGalleryMZ(80)
                    banner.indicator = CircleIndicator(requireContext())

                    update_recycle_view.layoutManager = LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false)
                    update_recycle_view.adapter = DetailedChapterAdapter(data.updateItemList)
                    update_recycle_view.addItemDecoration(ChapterItemDecoration(requireContext()))

                    spin_kit.visibility = View.GONE

                }
            )
        }catch (e: Exception){

            Timber.e(e)
            Toast.makeText(this.requireContext(), e.message, Toast.LENGTH_SHORT).show()

        }
    }

}