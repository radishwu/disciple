package com.monster.disciple

import okhttp3.Interceptor
import okhttp3.Response

class LoginInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
                .header("token", "appToken")
                .build()

        return chain.proceed(newRequest)
    }
}