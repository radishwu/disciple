package com.monster.disciple.interceptor

import android.content.Context
import com.monster.disciple.util.isAvailable
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.File

/**
 * 设置http请求缓存策略
 * 无网络使用缓存并设置失效时间
 * 有网络不适用缓存
 */
class HttpCacheInterceptor(var context: Context?, var cacheInvalidSec: Int?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!isAvailable(context)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
        }
        val response = chain.proceed(request)
        if (isAvailable(context)) {
            val maxAge = 0                     // 有网络时 设置缓存超时时间0个小时
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .removeHeader("Pragma")     // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build()
        } else {

            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$cacheInvalidSec")
                    .removeHeader("Pragma")
                    .build()
        }
        return response
    }

    companion object {
        fun getCache(context: Context, cacheSize: Long): Cache {
            val httpCacheDirectory = File(context.cacheDir, "responses")
            return Cache(httpCacheDirectory, cacheSize)         // 缓存空间10M
        }
    }
}