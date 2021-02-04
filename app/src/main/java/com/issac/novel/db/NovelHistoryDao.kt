package com.issac.novel.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface NovelHistoryDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg items: NovelHistory)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg items: NovelHistory): Int

    @Delete
    fun delete(vararg items: NovelHistory): Int

    @Query("DELETE FROM NovelHistory WHERE timestamp IN (SELECT timestamp FROM NovelHistory ORDER BY timestamp ASC LIMIT 1)")
    fun deleteTheOldest(): Int

    @Query("SELECT * FROM NovelHistory ORDER BY timestamp DESC ")
    fun query(): LiveData<Array<NovelHistory>>

    @Query("SELECT count(*) FROM NovelHistory")
    fun queryCount(): Int
}