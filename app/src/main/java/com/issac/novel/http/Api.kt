package com.issac.novel.http

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface Api{

    @GET("{path}")
    suspend fun fetch(@Path("path") path: String): ResponseBody


}