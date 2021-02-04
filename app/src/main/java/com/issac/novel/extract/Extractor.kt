package com.issac.novel.extract

interface Extractor<T>{

    fun extract(html: String): T
}