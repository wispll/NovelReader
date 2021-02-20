package com.issac.novel.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NetworkUtils
import com.issac.novel.Error.NetworkUnavailableError
import com.issac.novel.extract.HotItemsExtractor
import com.issac.novel.extract.Novel
import com.issac.novel.extract.UpdateItemExtractor
import com.issac.novel.http.HttpClient
import com.issac.novel.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber


class NovelModel: ViewModel(){

    private val mLiveDataMap: HashMap<String, MutableLiveData<Novel>> = HashMap()

    @Throws(Exception::class)
    fun getLiveData(path: String): MutableLiveData<Novel>? {

        var liveData: MutableLiveData<Novel>? = mLiveDataMap[path]
        if(liveData == null){
            if(!NetworkUtils.isConnected()) throw NetworkUnavailableError()

            liveData = loadData(path)
            mLiveDataMap[path] = liveData
        }

        return liveData
    }

    private fun loadData(path: String): MutableLiveData<Novel>{
        val result = MutableLiveData<Novel>()
        viewModelScope.launch(Util.coroutineExceptionHandler) {
            try {
                val html= HttpClient.api().fetch(path).string()
                val hotItemDeferred = async(Dispatchers.Default) { HotItemsExtractor().extract(html) }
                val updateItemDeferred = async(Dispatchers.Default) { UpdateItemExtractor().extract(html) }

                result.value = Novel(
                    hotItemDeferred.await(),
                    updateItemDeferred.await()
                )

            }catch (e: Exception){
                Timber.e(e)
            }
        }

        return result
    }

}


