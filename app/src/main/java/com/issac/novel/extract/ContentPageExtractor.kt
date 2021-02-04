package com.issac.novel.extract

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import org.jsoup.Jsoup

@Parcelize
data class Content(
    val title: String,
    val content: String,
    val last_chapter_href: String?,
    val next_chapter_href: String?
): Parcelable

class ContentExtractor: Extractor<Content>{
    override fun extract(html: String): Content {

        val result = mutableListOf<Content>()

        val document = Jsoup.parse(html)
        val title = document.select("title").text()
        val content = document.select("#content").text()
        val lastChapterHref = checkEnd(document.select(".bottem1 a:first-child").attr("href"))
        val nextChapterHref = checkEnd(document.select(".bottem1 a:eq(2)").attr("href"))

        return Content(title, content, lastChapterHref, nextChapterHref)

    }

    private fun checkEnd(str: String): String?{
        return if(!str.contains("html")) null else str
    }
}

