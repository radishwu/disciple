package com.monster.disciple.util

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.monster.disciple.Disciple
import com.monster.disciple.interceptor.HttpCacheInterceptor
import com.monster.disciple.interceptor.MultiHostInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun newRetrofitInstance(context: Context, builder: Disciple.Builder): Retrofit {

    val okHttpBuilder = OkHttpClient.Builder()
            .addInterceptor(MultiHostInterceptor())
            .addInterceptor(HttpCacheInterceptor(context.applicationContext, builder.cacheInvalidSec))
            .cache(HttpCacheInterceptor.getCache(context.applicationContext, builder.cacheSize))
            .addInterceptor(if (builder.isDebug) HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
            .addNetworkInterceptor(StethoInterceptor())
            .connectTimeout(builder.timeout, TimeUnit.SECONDS)
            .writeTimeout(builder.timeout, TimeUnit.SECONDS)

    builder.interceptors?.forEach { interceptor -> okHttpBuilder.addInterceptor(interceptor) }

    return Retrofit.Builder()
            .client(okHttpBuilder.build())
            .baseUrl(builder.baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}