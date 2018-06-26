package com.monster.disciple

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.monster.disciple.interceptor.HttpCacheInterceptor
import com.monster.disciple.interceptor.MultiHostInterceptor
import com.monster.disciple.util.newRetrofitInstance
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap
import java.util.concurrent.TimeUnit

class Disciple private constructor() {

    companion object {
        const val MULTI_DOMAIN_NAME = "Multi-Domain-Name"
        const val MULTI_DOMAIN_NAME_HEADER = "$MULTI_DOMAIN_NAME:"

        @Volatile
        private var retrofit: Retrofit? = null
        @Volatile
        private var instance: Disciple? = null
        @Volatile
        private var clientInstance: OkHttpClient? = null
        @Volatile
        private var auxiliaryRetrofit: MutableMap<String, Retrofit>? = null

        fun init(context: Context, builder: Builder) {
            instance = Disciple()

            val okHttpBuilder = OkHttpClient.Builder()
                    .addInterceptor(MultiHostInterceptor())
                    .addInterceptor(HttpCacheInterceptor(context.applicationContext, builder.cacheInvalidSec))
                    .cache(HttpCacheInterceptor.getCache(context.applicationContext, builder.cacheSize))
                    .addInterceptor(if (builder.isDebug) HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
                    .addNetworkInterceptor(StethoInterceptor())
                    .connectTimeout(builder.timeout, TimeUnit.SECONDS)
                    .writeTimeout(builder.timeout, TimeUnit.SECONDS)

            builder.interceptors?.forEach { interceptor -> okHttpBuilder.addInterceptor(interceptor) }

            clientInstance = okHttpBuilder.build()

            instance!!.setBaseUrl(builder.baseUrl)
        }

        fun initAuxiliaryRetrofit(context: Context, builder: Builder, retrofitInstanceKey: String) {
            val retrofit = newRetrofitInstance(context, builder)
            if (null == auxiliaryRetrofit) {
                auxiliaryRetrofit = HashMap()
            }
            auxiliaryRetrofit?.put(retrofitInstanceKey, retrofit)
        }

        fun getInstance(): Disciple {
            if (null == instance) {
                throw RuntimeException("must be init first!")
            }
            return instance as Disciple
        }

        fun <T> create(serviceClazz: Class<T>): T {
            if (null == retrofit) {
                throw RuntimeException("must be init first!")
            }
            return retrofit!!.create(serviceClazz)
        }

        fun <T> create(retrofitInstanceKey: String, serviceClazz: Class<T>): T {

            retrofit = auxiliaryRetrofit?.get(retrofitInstanceKey)

            if (null == retrofit) {
                throw RuntimeException("auxiliary retrofit must be init first!")
            }
            return retrofit!!.create(serviceClazz)
        }

        fun initRetrofit(baseUrl: String) {
            retrofit = Retrofit.Builder()
                    .client(clientInstance!!)
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
    }

    private var multiHostMap: HashMap<String, String>? = null
    private var baseUrl: String? = null

    fun setBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
        initRetrofit(baseUrl)
    }

    fun getBaseUrl(): String? = baseUrl

    fun getMultiHostMap(): MutableMap<String, String>? = multiHostMap

    fun removeBaseUrl(key: String) {
        multiHostMap?.remove(key)
    }

    fun addBaseUrl(key: String, baseUrl: String) {
        if (null == multiHostMap) {
            multiHostMap = HashMap()
        }
        multiHostMap?.put(key, baseUrl)
    }

    class Builder {

        var baseUrl: String = ""
        var timeout: Long = 30
        var interceptors: MutableList<Interceptor>? = null
        var isDebug: Boolean = false
        /**
         * 缓存过期时间，默认1天
         */
        internal var cacheInvalidSec = 60 * 60 * 24
        /**
         * 缓存空间，默认10m
         */
        internal var cacheSize = (10 * 1024 * 1024).toLong()

        fun baseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        fun timeout(timeout: Long): Builder {
            this.timeout = timeout
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {
            if (null == interceptors) interceptors = ArrayList()
            interceptors?.add(interceptor)
            return this
        }

        fun isDebug(isDebug: Boolean): Builder {
            this.isDebug = isDebug
            return this
        }

        fun cacheInvalidSec(cacheInvalidSec: Int): Builder {
            this.cacheInvalidSec = cacheInvalidSec
            return this
        }

        fun cacheSize(cacheSize: Long): Builder {
            this.cacheSize = cacheSize
            return this
        }
    }
}