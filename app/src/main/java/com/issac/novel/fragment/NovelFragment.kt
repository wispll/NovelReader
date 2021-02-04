package com.issac.novel.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.issac.novel.R
import com.issac.novel.adapter.GenusPagerAdapter
import com.issac.novel.extract.Genus
import com.issac.novel.model.GenusModel
import kotlinx.android.synthetic.main.fragment_novel.*
import timber.log.Timber


class NovelFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_novel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tab_layout.tabMode = TabLayout.MODE_SCROLLABLE

        try{
            val model: GenusModel = ViewModelProvider(requireActivity()).get(GenusModel::class.java)
            val liveData = model.getLiveData()

            liveData?.observe(viewLifecycleOwner,
                Observer<List<Genus>> { data ->
                    novel_view_pager.adapter = GenusPagerAdapter(this.activity as FragmentActivity, data)
                    TabLayoutMediator(tab_layout, novel_view_pager) { tab, position ->
                        tab.text = data?.get(position)?.name
                    }.attach()

                }
            )
        }catch (e: Exception){
            Timber.e(e)
            Toast.makeText(this.requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }

    }
}

