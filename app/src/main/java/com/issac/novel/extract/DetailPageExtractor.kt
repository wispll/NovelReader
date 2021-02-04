package com.issac.novel.extract

import org.jsoup.Jsoup

data class Detail(
    val title: String,
    val author: String,
    val intro: String,
    val image: String,
    val latest_chapter: String,
    val latest_chapter_href: String,
    val chapter_list: List<SimpleChapter>
)

data class SimpleChapter(
    val name: String,
    val href: String
)


class DetailExtractor: Extractor<Detail>{

    override fun extract(html: String): Detail {

        val document = Jsoup.parse(html)
        val image = document.select("div#fmimg").select("img").attr("src")
        val title = document.select("div#info").select("h1").text()
        val author = document.select("div#info > p").first().text()
        val intro = document.select("div#intro").text()
        val latestChapter = document.select("div#info > p:last-child a").text()
        val latestChapterHref = document.select("div#info > p:last-child a").attr("href")

        val chapterList = mutableListOf<SimpleChapter>()
        document.select("div#list dd").forEach {
            val a = it.select("a")
            val item = SimpleChapter(a.text(), a.attr("href"))
            chapterList.add(item)
        }

        return Detail(title,author,intro,image,latestChapter,latestChapterHref, chapterList)

    }

}
