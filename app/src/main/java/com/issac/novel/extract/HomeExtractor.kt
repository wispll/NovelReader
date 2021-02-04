package com.issac.novel.extract

import org.jsoup.Jsoup

data class Novel(
    val hotItemList: List<HotItem>,
    val updateItemList: List<DetailedChapterItem>
)

data class HotItem(
    val image: String,
    val name: String,
    val author: String,
    val intro: String,
    val href: String
)

data class Genus(
    val name: String,
    val href: String
)


data class DetailedChapterItem(
    val genus: String,
    val name: String,
    val chapter: String,
    val author: String,
    val chapter_href: String,
    val content_href: String
)

class HotItemsExtractor: Extractor<MutableList<HotItem>>{

    override fun extract(html: String): MutableList<HotItem> {
        val result = mutableListOf<HotItem>()

        val document = Jsoup.parse(html)
        val elements = document.select("div.item")

        elements.forEach{
            val image = it.select("img").attr("src")
            val name = it.select("dl a").text()
            val author = it.select("dl span").text()
            val intro = it.select("dl dd").text()
            val href = it.select("dt a").attr("href")

            result.add(HotItem(image,name,author,intro,href))
        }

        return result
    }

}

class GenusExtractor: Extractor<MutableList<Genus>>{

    override fun extract(html: String): MutableList<Genus> {
        val result = mutableListOf<Genus>()

        val document = Jsoup.parse(html)
        val elements = document.select("div.nav a")

        elements.forEachIndexed{ index, ele ->
            if(index > elements.size-3) return@forEachIndexed

            val name = ele.text()
            val href = ele.attr("href")

            result.add(Genus(name,href))
        }

        return result
    }

}

class UpdateItemExtractor: Extractor<MutableList<DetailedChapterItem>>{

    override fun extract(html: String): MutableList<DetailedChapterItem> {
        val result = mutableListOf<DetailedChapterItem>()

        val document = Jsoup.parse(html)
        val elements = document.select("div.l li")

        elements.forEach{

            val value = it.select("span.s1")
            val genus : String = if(!value.isEmpty()) value.text() else ""

            val s2 = it.select("span.s2")
            val name = s2.text()
            val chapter_href = s2.select("a").attr("href")

            val s3 = it.select("span.s3")
            val chapter = s3.text()
            val content_href = s3.select("a").attr("href")


            val authorElement = it.select("span.s4")
            val author  = if(!authorElement.isEmpty()) authorElement.text() else
              it.select("span.s5").text()

            result.add(DetailedChapterItem(genus, name, chapter, author, chapter_href, content_href))
        }

        return result
    }

}

