package com.issac.novel.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NetworkUtils
import com.issac.novel.Error.NetworkUnavailableError
import com.issac.novel.extract.Detail
import com.issac.novel.extract.DetailExtractor
import com.issac.novel.http.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailModel: ViewModel() {


    @Throws(Error::class)
    fun getLiveData(path: String): MutableLiveData<Detail>? {
        if(!NetworkUtils.isConnected()) throw NetworkUnavailableError()

        val liveData = MutableLiveData<Detail>()

        viewModelScope.launch {
            val html = HttpClient.api().fetch(path).string()
            val detail = withContext(Dispatchers.Default) {
                DetailExtractor().extract(html)
            }

            liveData.value = detail

        }

        return liveData
    }
}