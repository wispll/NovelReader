package com.issac.novel.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.issac.novel.fragment.BookshelfFragment
import com.issac.novel.fragment.NovelFragment

class BottomNavPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

    private val fragments: Array<Fragment> = arrayOf(NovelFragment(), BookshelfFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}

