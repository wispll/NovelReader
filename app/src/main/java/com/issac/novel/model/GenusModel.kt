package com.issac.novel.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NetworkUtils
import com.issac.novel.Error.NetworkUnavailableError
import com.issac.novel.extract.Genus
import com.issac.novel.extract.GenusExtractor
import com.issac.novel.http.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GenusModel : ViewModel() {

    var mLiveData: MutableLiveData<List<Genus>>? = null

    @Throws(Exception::class)
    fun getLiveData(): MutableLiveData<List<Genus>>? {
        if (mLiveData == null) {
            if(!NetworkUtils.isConnected()) throw NetworkUnavailableError()

            mLiveData = MutableLiveData()
            loadData()
        }
        return mLiveData
    }

    private fun loadData(){
        viewModelScope.launch {
            val html = HttpClient.api().fetch("").string()
            val genusList = withContext(Dispatchers.Default) {
                GenusExtractor().extract(html)
            }

            mLiveData?.value = genusList
        }
    }

}