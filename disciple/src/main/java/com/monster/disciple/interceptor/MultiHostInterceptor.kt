package com.monster.disciple.interceptor

import android.text.TextUtils
import com.monster.disciple.Disciple
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class MultiHostInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val multiHostKeys = originalRequest.headers(Disciple.MULTI_DOMAIN_NAME)

        if (null == multiHostKeys || 0 == multiHostKeys.size) {
            return chain.proceed(originalRequest)
        }

        val newBuilder = originalRequest.newBuilder()
        newBuilder.removeHeader(Disciple.MULTI_DOMAIN_NAME)
        val baseUrlKey = multiHostKeys[0]
        val newBaseUrl = Disciple.getInstance().getMultiHostMap()?.get(baseUrlKey)
        if (TextUtils.isEmpty(newBaseUrl)) {
            return chain.proceed(originalRequest)
        }
        val baseUrl = HttpUrl.parse(newBaseUrl!!) ?: return chain.proceed(originalRequest)

        val newHttpUrl = originalRequest.url().newBuilder()
                .scheme(baseUrl.scheme())
                .host(baseUrl.host())
                .port(baseUrl.port())
                .build()

        return chain.proceed(newBuilder.url(newHttpUrl).build())
    }
}