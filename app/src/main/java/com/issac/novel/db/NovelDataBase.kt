package com.issac.novel.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [NovelHistory::class], version = 1)
abstract class NovelDataBase: RoomDatabase() {

    abstract fun novelHistoryDao(): NovelHistoryDao

    companion object{

        lateinit var instance: NovelDataBase

        fun create(context: Context){
           instance = Room.databaseBuilder(
               context,NovelDataBase::class.java,"novel.db"
           ).build()
        }
    }
}