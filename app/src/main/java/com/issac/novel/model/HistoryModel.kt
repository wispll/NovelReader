package com.issac.novel.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issac.novel.db.NovelDataBase
import com.issac.novel.db.NovelHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryModel: ViewModel(){

    fun save2Db(item: NovelHistory){
        viewModelScope.launch {
            withContext(Dispatchers.Default){

                NovelDataBase.instance.novelHistoryDao().insert(item)
            }
        }
    }

    fun queryAll(): LiveData<Array<NovelHistory>>{
        return NovelDataBase.instance.novelHistoryDao().query()
    }
}