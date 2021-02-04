package com.issac.novel.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.issac.novel.extract.Genus
import com.issac.novel.fragment.GenusPageFragment

class GenusPagerAdapter(fa: FragmentActivity, private val data: List<Genus>): FragmentStateAdapter(fa){

    override fun getItemCount(): Int = data.size

    override fun createFragment(position: Int): Fragment {
        val fragment = GenusPageFragment()
        fragment.arguments = Bundle()
        fragment.requireArguments().putString(GenusPageFragment.HREF_ARG, data[position].href)
        return fragment
    }
}