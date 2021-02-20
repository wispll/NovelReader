package com.issac.novel.util

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.ToastUtils
import com.issac.novel.R
import com.issac.novel.fragment.Arguments
import com.issac.novel.fragment.ReaderFragment
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

class Util{



    companion object{

        val coroutineExceptionHandler = CoroutineExceptionHandler{_, t ->
            ToastUtils.showLong(t.message)
            Timber.e(t)
        }

        fun goReader(activity: FragmentActivity, arg: Arguments){
            val fragment = ReaderFragment()
            val bundle = Bundle()
            bundle.putParcelable("arg", arg)
            fragment.arguments  = bundle

            activity.supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, "readerFragment")
                .addToBackStack("reader")
                .commit()
        }

    }
}