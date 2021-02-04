package com.issac.novel.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.issac.novel.R
import com.issac.novel.adapter.BottomNavPagerAdapter
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment: Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_pager.adapter = BottomNavPagerAdapter(this.requireActivity())
        view_pager.isUserInputEnabled = false

        bottom_nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item_bookshelf -> {
                    view_pager.currentItem = 1
                    true
                }
                R.id.menu_item_novel -> {
                    view_pager.currentItem = 0
                    true
                }
                else -> false
            }
        }
    }
}