package com.issac.novel.http

import com.issac.novel.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class HttpClient {

    companion object {
        private const val BASE_URL = "https://www.biquge.com.cn"
        private const val TIME_OUT_SECONDS = 10

        private val interceptor: () -> HttpLoggingInterceptor =
            {
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Timber.d( message)
                    }
                }).apply {
                    level = when (BuildConfig.DEBUG) {
                        true -> HttpLoggingInterceptor.Level.BODY
                        false -> HttpLoggingInterceptor.Level.NONE
                    }
                }


            }

        private val okHttpClient: () -> OkHttpClient = {
            OkHttpClient.Builder()
                .connectTimeout(
                    TIME_OUT_SECONDS.toLong(),
                    TimeUnit.SECONDS
                )
                .readTimeout(
                    TIME_OUT_SECONDS.toLong(),
                    TimeUnit.SECONDS
                )
                .addInterceptor(interceptor())
                .build()
        }

        private fun <T : Any> createRetrofitImpl(t: Class<T>): T =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(t)


        fun api() = createRetrofitImpl(Api::class.java)

    }
}


