package com.issac.novel

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.issac.novel.fragment.MainFragment

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, MainFragment(), "mainFragment")
            .commit()
    }
}
