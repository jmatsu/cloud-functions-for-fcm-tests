package io.github.jmatsu.cf_dev_tool

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface HasNotificationService {
    fun bindNotificationService(): Lazy<NotificationService> {
        val okHttpClient = OkHttpClient()

        val retrofit = Retrofit.Builder().apply {
            baseUrl(BuildConfig.CLOUD_FUNCTION_ENDPOINT)
            addConverterFactory(GsonConverterFactory.create())
            client(okHttpClient)
        }.build()

        return lazyOf(retrofit.create(NotificationService::class.java))
    }
}