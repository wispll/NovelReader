package com.issac.novel.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NovelHistory (
        @PrimaryKey
        val id: String,
        val title: String,
        val author: String,
        val chapter: String,
        val href: String,
        val timestamp: Long
)