package com.example.composenewscatcher.data.remote.newscatcherapi

import com.example.composenewscatcher.util.constant.NewsCatcherApiConstants
import okhttp3.Interceptor
import okhttp3.Response

class NewsCatcherApiInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("x-api-key", NewsCatcherApiConstants.NEWS_API_KEY)
            .build()

        return chain.proceed(request)
    }
}