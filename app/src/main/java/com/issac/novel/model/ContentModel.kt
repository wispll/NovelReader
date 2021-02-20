package com.issac.novel.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NetworkUtils
import com.issac.novel.Error.NetworkUnavailableError
import com.issac.novel.extract.Content
import com.issac.novel.extract.ContentExtractor
import com.issac.novel.http.HttpClient
import com.issac.novel.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContentModel: ViewModel() {

    @Throws(Error::class)
    fun getLiveData(path: String): MutableLiveData<Content>? {
        if(!NetworkUtils.isConnected()) throw NetworkUnavailableError()

        val liveData = MutableLiveData<Content>()

        viewModelScope.launch(Util.coroutineExceptionHandler) {
            val html = HttpClient.api().fetch(path).string()
            val content = withContext(Dispatchers.Default) {
                ContentExtractor().extract(html)
            }

            liveData.value = content

        }

        return liveData
    }

}

